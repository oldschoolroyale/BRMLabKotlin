package com.brm.brmlabkotlin.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.brm.brmlabkotlin.model.DoctorModel
import com.brm.brmlabkotlin.provider.DoctorAddProvider
import com.brm.brmlabkotlin.view.DoctorAddView

@InjectViewState
class DoctorAddPresenter: MvpPresenter<DoctorAddView>() {
    fun load(array: Array<String>, hashMap: HashMap<String, String>){
        DoctorAddProvider(this).parse(array = array, hashMap = hashMap )
    }
    fun tryToSetup(error: String){
        viewState.goBack(error = error)
    }
}