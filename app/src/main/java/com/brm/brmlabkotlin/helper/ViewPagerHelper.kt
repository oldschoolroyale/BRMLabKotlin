package com.brm.brmlabkotlin.helper

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.adapter.ViewPagerAdapter

class ViewPagerHelper {

    fun setUpIndicator(viewPagerAdapter: ViewPagerAdapter,
                               indicatorLayout: LinearLayout,
    context: Context
    ){
        val indicators = arrayOfNulls<ImageView>(viewPagerAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8,0,8,0)
        for(i in indicators.indices){
            indicators[i] = ImageView(context)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            indicatorLayout.addView(indicators[i])
        }
    }

    fun setCurrentIndicator(index: Int,
                                    indicatorLayout: LinearLayout,
    context: Context){
        val childCount = indicatorLayout.childCount
        for (i in 0 until childCount){
            val imageView = indicatorLayout[i] as ImageView
            if (i == index){
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.indicator_active
                    )
                )
            }else{
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }

}