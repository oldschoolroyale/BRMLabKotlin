package com.brm.brmlabkotlin.provider

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.brm.brmlabkotlin.presenter.UpdateDoctorPresenter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class UpdateDoctorProvider(val presenter: UpdateDoctorPresenter, var array: Array<String>,
var context: Context) {
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
                    val type = snapshot.child("visit")
                    if (type.equals("Визит не окончен")){
                        startVisit()
                    }
                    else{
                        presenter.showError("Визит уже начат или окончен!")
                    }
                }
            })
        }, 2000)
    }

    fun startVisit(){
        val hashMap: HashMap<String, Double> = HashMap()
        val stringHash: HashMap<String, String> = HashMap()
        val fusedLocation = FusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocation.lastLocation.addOnSuccessListener {
            if (it != null){
                hashMap["lon"] = it.longitude
                hashMap["lat"] = it.latitude
            }
        }
        val calendar: Calendar = Calendar.getInstance()
        val mdFormat = SimpleDateFormat("HH:mm:ss")
        stringHash["time_start"] = mdFormat.format(calendar.time).toString()
        stringHash["visit"] = "Визит начат"
        reference.setValue(hashMap, stringHash).addOnCompleteListener {
            checkForSuccessful(it)
        }

    }

    private fun checkForSuccessful(it: Task<Void>){
        if (it.isSuccessful){presenter.showError("Успешно отправленно!")}
        else{presenter.showError("Ошибка при отправлении!")}
    }
}