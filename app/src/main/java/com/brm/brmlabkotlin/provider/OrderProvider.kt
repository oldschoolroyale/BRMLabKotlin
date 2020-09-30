package com.brm.brmlabkotlin.provider

import android.os.AsyncTask
import android.os.Handler
import com.brm.brmlabkotlin.model.OrderModel
import com.brm.brmlabkotlin.presenter.OrderPresenter
import com.google.firebase.database.*

class OrderProvider(val presenter: OrderPresenter): AsyncTask<String, Void, Void>() {
    fun parse(token: String){
        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
            .child("Account").child(token)
        Handler().postDelayed({
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                presenter.showError(error = error.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val medications = snapshot.child("medications").value.toString()
                execute(medications)
            }
        })}, 1500)

    }

    override fun doInBackground(vararg params: String?): Void?
    {
        val array = params[0]?.split(" ")
        val list: ArrayList<OrderModel> = ArrayList()

        for (i in array!!.indices){
            val reference = FirebaseDatabase.getInstance().reference
                .child("Medications").child(array[i])
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    presenter.showError(error = error.toString())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children){
                        val model = snap.getValue(OrderModel::class.java)
                        model?.quantity = 0
                        list.add(model!!)
                    }
                    if (i == array.count() - 1){
                        presenter.tryToLoad(list)
                    }
                }
            })
        }
     return null
    }

    class OrderSend(val presenter: OrderPresenter,
                    array: Array<String>, list: ArrayList<OrderModel>): AsyncTask<Void, Void, Void>(){
        var reference = FirebaseDatabase.getInstance().reference.child("Notes")
            .child(array[0])
            //Date
            .child(array[1]).child(array[2]).child(array[3])
        //Id
            .child(array[4])
        var sendList = list

        fun send(){
            Handler().postDelayed({
                execute()
            }, 1500)
        }

        override fun doInBackground(vararg params: Void?): Void? {
            val hashMap: HashMap<String, String> = HashMap()
                for (i in sendList.indices){
                    if (sendList[i].quantity != 0){
                        hashMap.clear()
                        hashMap["name"] = sendList[i].name.toString()
                        hashMap["quantity"] = sendList[i].quantity.toString()
                        reference.child("stock").child("item $i")
                            .setValue(hashMap)
                    }
                }
            return null
        }

        override fun onPostExecute(result: Void?) {
            presenter.goBack()
            super.onPostExecute(result)
        }
    }
}