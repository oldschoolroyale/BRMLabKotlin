package com.brm.brmlabkotlin.provider

import android.os.AsyncTask
import android.os.Handler
import com.brm.brmlabkotlin.model.OrderModel
import com.brm.brmlabkotlin.model.StockModel
import com.brm.brmlabkotlin.presenter.StockPresenter
import com.google.firebase.database.*

class StockProvider(val presenter: StockPresenter): AsyncTask<String, Void, Void>() {

    fun parse(token: String){
        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
            .child("Account").child(token)
        Handler().postDelayed({
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    presenter.showError(error = error.toString())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val medications = snapshot.child("medications").value.toString()
                    execute(medications)
                }
            })}, 1500)

    }

    override fun doInBackground(vararg params: String?): Void? {
        val array = params[0]?.split(" ")
        val list: ArrayList<StockModel> = ArrayList()

        for (i in array!!.indices){
            val reference = FirebaseDatabase.getInstance().reference
                .child("Medications").child(array[i])
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    presenter.showError(error = error.toString())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children){
                        val model = snap.getValue(StockModel::class.java)
                        model?.quantity = 0
                        list.add(model!!)
                    }
                    if (i == array.count() - 1){
                        presenter.tryToSetUp(list)
                    }
                }
            })
        }
        return null
    }

    class StockSend(val presenter: StockPresenter, array: Array<String>,
                    list: ArrayList<StockModel>): AsyncTask<Double, Void,  Void>(){
        var reference: DatabaseReference = FirebaseDatabase.getInstance().reference
            .child("Orders")
                //DATE
            .child(array[0]).child(array[1]).child(array[2])
                //ID
            .child(array[3])
        var sendList = list

        fun send(){
            execute()
        }
        override fun doInBackground(vararg params: Double?): Void? {
            val hashMap: HashMap<String, String> = HashMap()
            for(i in sendList.indices){
                if (sendList[i].quantity != 0){
                    TODO("Потом доделаю")
                }
            }
            return null
        }

    }

    class CalculateChoose(val presenter: StockPresenter, val type: String,
    val list: ArrayList<StockModel>): AsyncTask<Void, String, String>(){

        fun calculate(){
            execute()
        }

        override fun doInBackground(vararg params: Void?): String? {
            var finalPrice = 0.0
            for (i in list.indices){
                if (list[i].quantity != 0 ) {
                    finalPrice += (when (type) {
                        "full" -> {
                            list[i].full!! * list[i].quantity!!
                        }
                        "half" -> {
                            (list[i].half!! * list[i].quantity!!) * 0.5
                        }
                        else -> {
                            (list[i].semi!! * list[i].quantity!!) * 0.25
                        }
                    })
                }
            }
            return "Предоплата состовляет : $finalPrice"
        }

        override fun onPostExecute(result: String) {
            presenter.showCalculation(result)
            super.onPostExecute(result)
        }
    }
}