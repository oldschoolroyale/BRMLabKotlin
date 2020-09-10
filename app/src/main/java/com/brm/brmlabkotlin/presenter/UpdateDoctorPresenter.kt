package com.brm.brmlabkotlin.presenter

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.brm.brmlabkotlin.provider.UpdateDoctorProvider
import com.brm.brmlabkotlin.view.UpdateDoctor

@InjectViewState
class UpdateDoctorPresenter: MvpPresenter<UpdateDoctor>() {
    fun sendInformation(comment: String, array: Array<String>, context: Context){
        UpdateDoctorProvider(this, array, context).parse(comment)
    }
    fun startVisit(array: Array<String>, context: Context){
        UpdateDoctorProvider(this, array, context).parseVisit()
    }
    fun showError(error: String){
        viewState.showError(error = error)
    }
}