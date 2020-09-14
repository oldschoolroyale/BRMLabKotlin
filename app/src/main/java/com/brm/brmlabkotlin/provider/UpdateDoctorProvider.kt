package com.brm.brmlabkotlin.provider

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.util.Log
import androidx.core.app.ActivityCompat
import com.brm.brmlabkotlin.presenter.UpdateDoctorPresenter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class UpdateDoctorProvider(val presenter: UpdateDoctorPresenter) {


    fun parse(comment: String, array: Array<String>){
        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
            //Ref
            .child("Notes").child(array[0])
            //Date
            .child(array[1]).child(array[2]).child(array[3])
            //Id
            .child(array[4])
        reference.child("comment").setValue(comment).addOnCompleteListener {
            presenter.showError(checkForSuccessful(it))
        }
    }


     private fun checkForSuccessful(it: Task<Void>): String{
        if (it.isSuccessful)
            return "Успешно отправленно!"
        else
            return "Ошибка при отправлении!"
    }
}