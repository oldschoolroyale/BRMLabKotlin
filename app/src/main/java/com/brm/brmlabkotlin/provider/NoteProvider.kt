package com.brm.brmlabkotlin.provider

import android.os.AsyncTask
import android.util.Log
import com.brm.brmlabkotlin.model.NoteModel
import com.brm.brmlabkotlin.model.ViewPagerModel
import com.brm.brmlabkotlin.presenter.NotePresenter
import com.google.firebase.database.*

class NoteProvider(var presenter: NotePresenter): AsyncTask<Boolean, Void, Void>() {
    private lateinit var blankReference: DatabaseReference
    private lateinit var array: List<String>

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
        array = title.split(" ")
        if (array.count() > 1){
            execute(false)

        }
        else{
            blankReference = FirebaseDatabase.getInstance().reference.child("Blank")
                .child(title)
            execute(true)
        }

    }

    private fun parseOneTime(){
        val viewList: ArrayList<ViewPagerModel> = ArrayList()
        blankReference.addListenerForSingleValueEvent(object : ValueEventListener{
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

    private fun parseMoreTimes(){

        val viewList: ArrayList<ViewPagerModel> = ArrayList()

        for (a in array.indices){
            blankReference = FirebaseDatabase.getInstance().reference.child("Blank")
                .child(array[a])
            blankReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    presenter.showError(error = error.toString())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children){
                        val  model = i.getValue(ViewPagerModel::class.java)
                        viewList.add(model!!)
                    }
                    if (a == array.count() - 1)
                        presenter.viewPagerLoad(viewList)
                }
            })
        }
    }

    override fun doInBackground(vararg params: Boolean?): Void? {
        if (params[0]!!)
            parseOneTime()
        else
            parseMoreTimes()
        return null
    }
}