package com.brm.brmlabkotlin.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ProfileView: MvpView {
    fun startLoading()
    fun endLoading()
    fun setUpProfile(image: String, name: String)
    fun sendPhoto()
    fun putPhoto()
    fun showError(error: String)
}