package com.brm.brmlabkotlin.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.brm.brmlabkotlin.model.NoteModel
import com.brm.brmlabkotlin.provider.NoteProvider
import com.brm.brmlabkotlin.view.NoteView

@InjectViewState
class NotePresenter : MvpPresenter<NoteView>() {
    fun firstLoad(token: String, year: String, month: String, day: String){
        viewState.startLoading()
        NoteProvider(this).parseNotes(token, year, month, day)
        NoteProvider(this).parseImage(token = token)
    }
    fun load(token: String, year: String, month: String, day: String){
        viewState.startLoading()
        NoteProvider(this).parseNotes(token, year, month, day)
    }
    fun setUpList(list: ArrayList<NoteModel>){
        if (list.isEmpty()){
            viewState.loadEmptyList()
        }
        else{
            viewState.loadList(list = list)
        }
        viewState.endLoading()
        viewState.loadList(list = list)
    }

    fun setUpAccount(array: Array<String>){
        viewState.setUpAccount(array = array)
    }

    fun showError(error: String){
        viewState.endLoading()
        viewState.showError(error = error)
    }

}