package com.brm.brmlabkotlin.provider

import com.brm.brmlabkotlin.presenter.PharmacyAddPresenter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PharmacyAddProvider(val presenter: PharmacyAddPresenter) {
    fun parse(array: Array<String>, hashMap: HashMap<String, String>){
        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
            .child("DataBase").child(array[0]).child(array[1])
            .child("Pharmacy").child(hashMap["id"]!!)
        reference.setValue(hashMap).addOnCompleteListener {
            if(it.isSuccessful){
                presenter.endLoading("Успешно добавленно!")
            }
            else{
                presenter.endLoading("Ошибка при добавлении!")
            }
        }
    }
}