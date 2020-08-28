package com.brm.brmlabkotlin.provider

import com.brm.brmlabkotlin.activities.RegistrationActivity
import com.brm.brmlabkotlin.presenter.RegistrationPresenter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider


class RegistrationProvider(var presenter: RegistrationPresenter) : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        mAuth.signInWithCredential(p0)
            .addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful){
                    presenter.loadAccount()
                }
                else{
                    presenter.loadError("Возникла ошибка OTP")
                }
            })

    }

    override fun onVerificationFailed(p0: FirebaseException) {
        presenter.loadError(p0.toString())
    }

    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
        super.onCodeSent(p0, p1)
        android.os.Handler().postDelayed({
            presenter.codeSend(p0)
        }, 10000)

    }
}