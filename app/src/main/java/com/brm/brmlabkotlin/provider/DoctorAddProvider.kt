package com.brm.brmlabkotlin.provider

import com.brm.brmlabkotlin.presenter.DoctorAddPresenter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DoctorAddProvider(val presenter: DoctorAddPresenter) {
    fun parse(array: Array<String>, hashMap: HashMap<String, String>){

        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
            .child("DataBase").child(array[0]).child(array[1])
            .child("Doctor").child(hashMap["id"]!!)
        reference.setValue(hashMap).addOnCompleteListener {
            if(it.isSuccessful){
                presenter.tryToSetup("Успешно добавленно!")
            }
            else{
                presenter.tryToSetup("Ошибка при добавлении!")
            }
        }
    }
}