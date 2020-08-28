package com.brm.brmlabkotlin.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.brm.brmlabkotlin.provider.PharmacyAddProvider
import com.brm.brmlabkotlin.view.PharmacyAddView

@InjectViewState
class PharmacyAddPresenter: MvpPresenter<PharmacyAddView>() {
    fun startLoading(array: Array<String>, hashMap: HashMap<String, String>){
        PharmacyAddProvider(this).parse(array = array, hashMap = hashMap)
    }
    fun endLoading(error: String){
        viewState.showError(error = error)
    }
}