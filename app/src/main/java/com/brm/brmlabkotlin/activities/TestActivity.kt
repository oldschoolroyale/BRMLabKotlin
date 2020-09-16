package com.brm.brmlabkotlin.activities

import android.Manifest
import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.services.BackgroundService
import com.brm.brmlabkotlin.services.BackgroundService.LocalBinder
import com.brm.brmlabkotlin.services.Common
import com.brm.brmlabkotlin.services.SendLocationToActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_test.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class TestActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private var mService: BackgroundService? = null
    private var mBound: Boolean = false
    private lateinit var startBtn: Button
    private lateinit var stopBtn: Button

    private var user = FirebaseAuth.getInstance().currentUser?.uid

    //date
    private var yearString: String = SimpleDateFormat("yyyy").format(Calendar.getInstance().time)
    private var monthString: String = SimpleDateFormat("M").format(Calendar.getInstance().time)
    private var dayString: String = SimpleDateFormat("d").format(Calendar.getInstance().time)

    private lateinit var reference: DatabaseReference
    private lateinit var hashMap: HashMap<String, Double>

    private var mServiceConnection: ServiceConnection = object : ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
            mBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as LocalBinder
            mService = binder.service
            mBound = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        reference = FirebaseDatabase.getInstance()
            .reference.child("Notes").child(user!!)
            .child(yearString).child(monthString).child(dayString)

        Dexter.withActivity(this)
            .withPermissions(
                listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            )
            .withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    startBtn = findViewById(R.id.activity_test_start)
                    stopBtn = findViewById(R.id.activity_test_stop)

                    startBtn.setOnClickListener {
                            mService?.requestLocationUpdates()
                    }
                    stopBtn.setOnClickListener {
                        mService?.removeLocationUpdates()
                    }
                    setButtonState(Common().requestingLocationUpdates(applicationContext))
                    bindService(Intent(applicationContext, BackgroundService::class.java),
                        mServiceConnection,
                    Context.BIND_AUTO_CREATE)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    TODO("Not yet implemented")
                }
            }).check()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val common = Common()
        if (key.equals(common.KEY_REQUESTING_LOCATION_UPDATES))
        {
            setButtonState(sharedPreferences?.getBoolean(common.KEY_REQUESTING_LOCATION_UPDATES, false))

        }
    }

    private fun setButtonState(boolean: Boolean?) {
        if (boolean!!){
            activity_test_start.isEnabled = false
            activity_test_stop.isEnabled = true
        }
        else{
            activity_test_start.isEnabled = true
            activity_test_stop.isEnabled = false
        }
    }

    override fun onStart() {
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .registerOnSharedPreferenceChangeListener(this)
        EventBus.getDefault().register(this)
        super.onStart()

    }

    override fun onStop() {
        if (mBound){
            unbindService(mServiceConnection)
            mBound = false
        }
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)

        EventBus.getDefault().unregister(this)
        super.onStop()

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onListenLocation(event: SendLocationToActivity){
        if (event != null){
            val data = StringBuilder()
                .append(event.location?.longitude)
                .append("/")
                .append(event.location?.latitude)
            Toast.makeText(applicationContext, data, Toast.LENGTH_LONG).show()
        }
    }

}