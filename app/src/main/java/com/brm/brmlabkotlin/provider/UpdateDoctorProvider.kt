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

class UpdateDoctorProvider(val presenter: UpdateDoctorPresenter, var array: Array<String>,
var context: Context): AsyncTask<Void, Void, Void>() {
    private var reference: DatabaseReference = FirebaseDatabase.getInstance().reference
        //Ref
        .child("Notes").child(array[0])
        //Date
        .child(array[1]).child(array[2]).child(array[3])
        //Id
        .child(array[4])

    fun parse(comment: String){
        reference.child("comment").setValue(comment).addOnCompleteListener {
            checkForSuccessful(it)
        }
    }

    fun parseVisit(){
        android.os.Handler().postDelayed({
            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    presenter.showError("Ошибка при подключении к дата базе")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    execute()
                }
            })
        }, 2000)
    }

    private fun checkForSuccessful(it: Task<Void>){
        if (it.isSuccessful){presenter.showError("Успешно отправленно!")}
        else{presenter.showError("Ошибка при отправлении!")}
    }

    override fun onPreExecute() {
        super.onPreExecute()
        presenter.showError("Начинаю")
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        presenter.showError("Конец")
    }

    override fun doInBackground(vararg params: Void?): Void? {
        try {
            val fusedLocation = FusedLocationProviderClient(context)
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            )
            fusedLocation.lastLocation.addOnSuccessListener {
                if (it != null){
                    val lon = it.longitude
                    var lat = it.latitude
                    Log.d("MyLog", "ya tut byl")
                }
                else{
                    Log.d("MyLog", "fused null")
                }
            }
            val calendar: Calendar = Calendar.getInstance()
            val mdFormat = SimpleDateFormat("HH:mm:ss")
            reference.child("time_start") .setValue(mdFormat.format(calendar.time).toString())
            reference.child("visit").setValue("Визит начат")
        }
        catch (e: InterruptedException){
            e.printStackTrace()
        }
        return null
    }
}