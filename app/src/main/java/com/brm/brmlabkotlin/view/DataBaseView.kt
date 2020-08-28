package com.brm.brmlabkotlin.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.brm.brmlabkotlin.model.DataBaseModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface DataBaseView : MvpView {
    fun startLoading()
    fun endLoading()
    fun showError(error: String)
    fun loadList(list: ArrayList<DataBaseModel>)
    fun loadEmptyList()
    fun itemClick(position: Int)
}