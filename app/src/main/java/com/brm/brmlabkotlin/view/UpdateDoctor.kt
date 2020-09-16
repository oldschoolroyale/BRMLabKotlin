package com.brm.brmlabkotlin.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface UpdateDoctor: MvpView {
    fun showError(error: String)
    fun goBack(message: String)
}