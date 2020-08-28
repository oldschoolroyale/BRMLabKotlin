package com.brm.brmlabkotlin.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.brm.brmlabkotlin.activities.MainActivity
import com.brm.brmlabkotlin.activities.RegistrationActivity
import com.brm.brmlabkotlin.provider.RegistrationProvider
import com.brm.brmlabkotlin.view.RegistrationView
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@InjectViewState
class RegistrationPresenter : MvpPresenter<RegistrationView>() {

    fun prepare(phoneNumber : String){
        viewState.startLoading()

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            RegistrationActivity(),
            RegistrationProvider(this)
        )
    }

    fun loadAccount(){
        viewState.endLoading()
        viewState.openMain()
    }

    fun loadError(error : String){
        viewState.endLoading()
        viewState.showError(error)
    }

    fun codeSend(code : String){
        viewState.endLoading()
        viewState.openLogin(code)
    }
}

