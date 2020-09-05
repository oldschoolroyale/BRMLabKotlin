package com.brm.brmlabkotlin.presenter

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.brm.brmlabkotlin.provider.UpdateDoctorProvider
import com.brm.brmlabkotlin.view.UpdateDoctor

@InjectViewState
class UpdateDoctorPresenter(var array: Array<String>, var context: Context): MvpPresenter<UpdateDoctor>() {
    var updateDoctorProvider = UpdateDoctorProvider(this, array, context)
    fun sendInformation(comment: String){
        updateDoctorProvider.parse(comment)
    }
    fun startVisit(){
        updateDoctorProvider.parseVisit()
    }
    fun showError(error: String){
        viewState.showError(error = error)
    }
}