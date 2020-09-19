package com.brm.brmlabkotlin.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.brm.brmlabkotlin.model.OrderModel
import com.brm.brmlabkotlin.provider.OrderProvider
import com.brm.brmlabkotlin.view.OrderView

@InjectViewState
class OrderPresenter: MvpPresenter<OrderView>() {
    fun load(token: String){
        viewState.startLoading()
        OrderProvider(this).parse(token)
    }
    fun showError(error:String){
        viewState.endLoading()
        viewState.showError(error= error)
    }
    fun tryToLoad(list: ArrayList<OrderModel>){
        viewState.endLoading()
        if(list.isEmpty()){
            viewState.loadEmptyList()
        }
        else{
            viewState.loadList(list = list)
        }
    }

    fun sendInformation(array: Array<String>, list: ArrayList<OrderModel>){
        viewState.startLoading()
        OrderProvider.OrderSend(this, array, list).send()
    }
    fun goBack(){
        viewState.endLoading()
        viewState.goBack()
    }

}