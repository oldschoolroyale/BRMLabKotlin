package com.brm.brmlabkotlin.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.brm.brmlabkotlin.provider.UpdatePharmacyProvider
import com.brm.brmlabkotlin.view.UpdateDoctor

@InjectViewState
class UpdatePharmacyPresenter: MvpPresenter<UpdateDoctor>() {

    fun send(comment: String, array: Array<String>){
        UpdatePharmacyProvider(this, array).parse(comment)
    }

    fun showError(error: String){
        viewState.showError(error)
    }

    fun delete(error: String){
        viewState.goBack(error)
    }

    fun startDelete(array: Array<String>){
        UpdatePharmacyProvider(this, array).parseDelete()
    }
}