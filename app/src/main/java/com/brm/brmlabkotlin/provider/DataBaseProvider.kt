package com.brm.brmlabkotlin.provider
import com.brm.brmlabkotlin.model.DataBaseModel
import com.brm.brmlabkotlin.presenter.DataBasePresenter
import com.google.firebase.database.*

class DataBaseProvider(val presenter: DataBasePresenter) {
    fun parse(type: String, town: String, region: String){
        val loadList: ArrayList<DataBaseModel> = ArrayList()
        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("DataBase").child(town).child(region).child(type)
        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                presenter.showError(error = error.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                loadList.clear()
                for (i in snapshot.children){
                    val data = i.getValue(DataBaseModel::class.java)
                    loadList.add(data!!)
                }
                presenter.endLoading(loadList)
            }

        })
    }
}