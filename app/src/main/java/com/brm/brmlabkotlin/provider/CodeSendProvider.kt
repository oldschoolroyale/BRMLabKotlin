package com.brm.brmlabkotlin.provider

import android.util.Log
import com.brm.brmlabkotlin.presenter.CodeSendPresenter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential

class CodeSendProvider(var presenter: CodeSendPresenter) {
    fun load(credential: PhoneAuthCredential){
        Log.d("MyLog", credential.toString())
        val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful){
                    presenter.providedData(true)
                }
                else{
                    presenter.providedData(false)
                }
            })
    }
}