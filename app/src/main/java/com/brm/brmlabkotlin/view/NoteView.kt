package com.brm.brmlabkotlin.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.brm.brmlabkotlin.model.NoteModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface NoteView : MvpView{
    fun startLoading()
    fun endLoading()
    fun showError(error: String)
    fun loadEmptyList()
    fun setUpAccount(array: Array<String>)
    fun loadList(list: ArrayList<NoteModel>)
    fun itemClick(position: Int)
    fun viewPagerClick(position: Int)
}