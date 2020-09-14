package com.brm.brmlabkotlin.presenter

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.brm.brmlabkotlin.provider.UpdateDoctorProvider
import com.brm.brmlabkotlin.view.UpdateDoctor

@InjectViewState
class UpdateDoctorPresenter: MvpPresenter<UpdateDoctor>() {
    fun sendInformation(comment: String, array: Array<String>){
        UpdateDoctorProvider(this).parse(comment, array)
    }
    fun showError(error: String){
        viewState.showError(error = error)
    }
}