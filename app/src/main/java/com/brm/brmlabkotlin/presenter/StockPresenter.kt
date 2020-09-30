package com.brm.brmlabkotlin.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.brm.brmlabkotlin.model.StockModel
import com.brm.brmlabkotlin.provider.StockProvider
import com.brm.brmlabkotlin.view.StockView

@InjectViewState
class StockPresenter: MvpPresenter<StockView>() {
    fun load(user: String){
        viewState.startLoading()
        StockProvider(this).parse(user)
    }
    fun tryToSetUp(list: ArrayList<StockModel>){
        viewState.endLoading()
        if (list.isEmpty()){
            viewState.loadEmptyLayout()
        }
        else{
            viewState.loadList(list)
        }
    }

    fun showError(error: String){
        viewState.showError(error)
    }

    fun startCalculation(list: ArrayList<StockModel>, type: String){
        StockProvider.CalculateChoose(this, type, list).calculate()
    }

    fun showCalculation(message: String){
        viewState.showAlert(message)
    }
}