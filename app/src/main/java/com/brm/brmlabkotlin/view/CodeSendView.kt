package com.brm.brmlabkotlin.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface CodeSendView : MvpView {
    fun startLoading()
    fun endLoading()
    fun showError(error : String)
    fun openProfile()
    fun resendCode()
}