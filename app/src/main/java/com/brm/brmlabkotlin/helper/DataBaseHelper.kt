package com.brm.brmlabkotlin.helper

import android.content.Context
import android.content.Intent
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.brm.brmlabkotlin.activities.NoteAddActivity
import com.brm.brmlabkotlin.model.DataBaseModel
import com.brm.brmlabkotlin.model.DoctorModel
import java.util.*

class DataBaseHelper {


    fun sendInformation(array: ArrayList<DataBaseModel>, position: Int, check: Boolean, context: Context?)
    {
        val increasingInt = Random().nextInt()
        val intent = Intent(context, NoteAddActivity::class.java)
        if (check){
            val doctorModel = DoctorModel(array[position].address,
                "null", "Item$increasingInt", "null", "null", "null",
                array[position].name, "null", "null", "Визит к врачу",
                "Визит не окончен")
            intent.putExtra("model", doctorModel)
            intent.putExtra("id", "Item$increasingInt")
        }
        else{
            val pharmacyModel = DoctorModel(array[position].address,
                "null", "Item$increasingInt", "null", "null", "null",
                array[position].name, "null", "null", "Визит в аптеку",
                "Визит не окончен")
            intent.putExtra("model", pharmacyModel)
            intent.putExtra("id", "Item$increasingInt")
        }
        context?.startActivity(intent)

    }

}