package com.brm.brmlabkotlin.provider

import com.brm.brmlabkotlin.model.NoteModel
import com.brm.brmlabkotlin.model.ViewPagerModel
import com.brm.brmlabkotlin.presenter.NotePresenter
import com.google.firebase.database.*

class NoteProvider(var presenter: NotePresenter) {
    fun parseImage(token: String){
        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Account")
            .child(token)
        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                presenter.showError(error = error.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val image = snapshot.child("image").value.toString()
                val name = snapshot.child("name").value.toString()
                val town = snapshot.child("town").value.toString()
                val region = snapshot.child("region").value.toString()
                parseViewPager(snapshot.child("medications").value.toString())
                presenter.setUpAccount(arrayOf(image, name, town, region))
            }
        })
    }

    fun parseNotes(token: String, year: String, month: String, day: String){
        val loadList: ArrayList<NoteModel> = ArrayList()
        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Notes")
            .child(token).child(year).child(month).child(day)
        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                presenter.showError(error = error.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    val model = i.getValue(NoteModel::class.java)
                    loadList.add(model!!)
                }
                presenter.setUpList(list = loadList)
            }
        })
    }

    fun parseViewPager(title: String){
        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Blank")
            .child(title)
        val viewList: ArrayList<ViewPagerModel> = ArrayList()
        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                presenter.showError(error = error.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    val  model = i.getValue(ViewPagerModel::class.java)
                    viewList.add(model!!)
                }
                presenter.viewPagerLoad(viewList)
            }
        })
    }
}