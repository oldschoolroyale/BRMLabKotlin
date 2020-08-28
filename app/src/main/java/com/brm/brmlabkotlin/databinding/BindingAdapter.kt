package com.brm.brmlabkotlin.databinding

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.brm.brmlabkotlin.R

class BindingAdapter {
    companion object{

        @BindingAdapter("android:navigateToCodeFragment")
        @JvmStatic
        fun navigateToCodeFragment(view: CircularProgressButton, navigate: Boolean){
            view.setOnClickListener{
                if (navigate){
                    view.findNavController().navigate(R.id.action_registrationFragment_to_codeSendFragment)
                }
            }
        }

        @BindingAdapter("android:emptyEditText")
        @JvmStatic
        fun emptyEditText(button: CircularProgressButton, clickable: Boolean){
            if (clickable){
                button.setBackgroundResource(R.drawable.button_shape_blue)
            }
            else{
                button.setBackgroundResource(R.drawable.button_shape_grey)
            }
        }
    }
}