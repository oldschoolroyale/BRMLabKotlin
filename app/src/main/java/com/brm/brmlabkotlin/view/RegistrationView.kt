package com.brm.brmlabkotlin.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface RegistrationView : MvpView {
    fun startLoading()
    fun endLoading()
    fun showError(textRecourse: String)
    fun openLogin(code : String)
    fun openMain()
}