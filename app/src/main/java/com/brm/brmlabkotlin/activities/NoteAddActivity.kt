package com.brm.brmlabkotlin.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.brm.brmlabkotlin.R

class NoteAddActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_note_add)
        val model = intent.getSerializableExtra("model")

    }
}