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
}