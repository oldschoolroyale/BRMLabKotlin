package com.brm.brmlabkotlin.presenter

import android.net.Uri
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.brm.brmlabkotlin.provider.ProfileProvider
import com.brm.brmlabkotlin.view.ProfileView

@InjectViewState
class ProfilePresenter: MvpPresenter<ProfileView>() {
    fun load(token: String){
        viewState.startLoading()
        ProfileProvider(this).parse(token = token)
    }
    fun tryToLoad(image: String, name: String){
        viewState.endLoading()
        viewState.putPhoto()
        viewState.setUpProfile(image, name)
    }
    fun showError(error: String){
        viewState.showError(error = error)
    }
    fun newPhoto(resultUri: Uri, token: String){
        viewState.sendPhoto()
        ProfileProvider(this).sendNewPhoto(resultUri, token)
    }

}