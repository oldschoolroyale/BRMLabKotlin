package com.brm.brmlabkotlin.provider

import android.net.Uri
import android.os.Handler
import com.brm.brmlabkotlin.presenter.ProfilePresenter
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class ProfileProvider(val presenter: ProfilePresenter) {
    fun parse(token: String){
        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Account")
            .child(token)
        reference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                presenter.showError(error = error.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val image = snapshot.child("image").value.toString()
                val name = snapshot.child("name").value.toString()
                presenter.tryToLoad(image, name)
            }
        })
    }

    fun sendNewPhoto(resultUri: Uri, token: String){
        Handler().postDelayed({
            val storageReference: StorageReference = FirebaseStorage.getInstance().reference
                .child("profile_images").child(random() + ".jpg")
            storageReference.putFile(resultUri).addOnCompleteListener {
                storageReference.downloadUrl.addOnSuccessListener { it ->
                    val downloadUrl = it.toString()
                    val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
                        .child("Account").child(token)
                    reference.child("image").setValue(downloadUrl).addOnCompleteListener {
                        parse(token = token)
                    }
                }
            }
        }, 2500)
    }

    private fun random(): String{
        val generator = Random()
        val randomStringBuilder = StringBuilder()
        val randomLength = generator.nextInt(15)
        var tempChar: Char
        for (i in 0 until randomLength) {
            tempChar = (generator.nextInt(96) + 32).toChar()
            randomStringBuilder.append(tempChar)
        }
        return randomStringBuilder.toString()
    }

    }
