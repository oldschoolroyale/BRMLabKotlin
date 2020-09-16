package com.brm.brmlabkotlin.provider

import com.brm.brmlabkotlin.presenter.UpdatePharmacyPresenter
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdatePharmacyProvider(val presenter: UpdatePharmacyPresenter, array: Array<String>) {

    private var reference: DatabaseReference = FirebaseDatabase.getInstance().reference
        //Token
        .child("Notes").child(array[0])
        //Date
        .child(array[1]).child(array[2]).child(array[3])
        //NoteId
        .child(array[4])

    fun parse(comment: String){
        reference.child("comment").setValue(comment).addOnCompleteListener {
            presenter.showError(checkForSuccessful(it))
        }
    }

    private fun checkForSuccessful(it: Task<Void>): String{
        return if (it.isSuccessful)
            "Успешно отправленно!"
        else
            "Ошибка при отправлении!"
    }

    fun parseDelete(){
        reference.removeValue().addOnCompleteListener {
            presenter.delete(checkForSuccessful(it))
        }
    }
}