package com.brm.brmlabkotlin.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.brm.brmlabkotlin.model.StockModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface StockView : MvpView{
    fun startLoading()
    fun endLoading()
    fun showError(error:String)
    fun loadEmptyLayout()
    fun loadList(list: ArrayList<StockModel>)
    fun goBack()
    fun showAlert(message: String)
}