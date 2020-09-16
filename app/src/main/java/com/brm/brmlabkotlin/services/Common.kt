package com.brm.brmlabkotlin.services

import android.content.Context
import android.location.Location
import android.preference.PreferenceManager
import java.text.SimpleDateFormat
import java.util.*


class Common  {
    val KEY_REQUESTING_LOCATION_UPDATES = "LocationUpdateEnable"

    fun getLocationText(mLocation: Location): String{
        return if (mLocation == null) "Неизвестная локация" else StringBuilder()
            .append(mLocation.latitude).append("/").append(mLocation.longitude).toString()
    }

    fun getLocationTitle(backgroundService: BackgroundService,
    mdFormat: SimpleDateFormat, calendar: Calendar): CharSequence? {

        return String.format("Локация обновленна: ${mdFormat.format(calendar.time)}")

    }

    fun setRequestLocationUpdates(context: Context, b: Boolean) {
        PreferenceManager.
                getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, b)
            .apply()
    }

    fun requestingLocationUpdates(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false)
    }


}