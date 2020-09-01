package com.brm.brmlabkotlin.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.brm.brmlabkotlin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.android.synthetic.main.fragment_note_add.*
import java.text.SimpleDateFormat
import java.util.*


class NoteAddActivity: MvpAppCompatActivity() {

    //date
    private var yearString: String = SimpleDateFormat("yyyy").format(Calendar.getInstance().time)
    private var monthString: String = SimpleDateFormat("M").format(Calendar.getInstance().time)
    private var dayString: String = SimpleDateFormat("d").format(Calendar.getInstance().time)

    private lateinit var dateText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_note_add)
        val model = intent.getSerializableExtra("model")
        val id = intent.getStringExtra("id")
        val mCurrentUser = FirebaseAuth.getInstance().currentUser?.uid

        dateText = findViewById(R.id.fragment_note_add_picked_text)
        setTextToTextView()

        fragment_note_add_button.setOnClickListener {
            val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
                .child("Notes").child(mCurrentUser.toString())
                .child(yearString).child(monthString).child(dayString).child(id!!)
            reference.setValue(model).addOnCompleteListener {
                if (it.isSuccessful) {
                    goBack("Успешно отправленно!")
                } else {
                    goBack("Ошибка!")
                }
            }
        }

        fragment_note_add_calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            yearString = year.toString()
            monthString = (month + 1).toString()
            dayString = dayOfMonth.toString()
            setTextToTextView()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTextToTextView() {
        dateText.text = "Выбранно $dayString/$monthString/$yearString"
    }

    private fun goBack(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_LONG).show()
        finish()
    }
}