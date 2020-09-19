package com.brm.brmlabkotlin.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.brm.brmlabkotlin.model.OrderModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface OrderView : MvpView {
    fun startLoading()
    fun endLoading()
    fun loadList(list: ArrayList<OrderModel>)
    fun loadEmptyList()
    fun showError(error: String)
    fun goBack()
}