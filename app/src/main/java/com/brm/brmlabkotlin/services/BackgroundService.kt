package com.brm.brmlabkotlin.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.brm.brmlabkotlin.activities.TestActivity
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class BackgroundService : Service() {

    companion object{
        private val CHANNEL_ID: String = "my_channel"
        private val UPDATE_INTERVAL_IN_MIL: Long = 60000
        private val FASTEST_UPDATE_INTERVAL_IN_MIL = UPDATE_INTERVAL_IN_MIL/2
        private val NOTI_ID = 1223
        private val EXTRA_STARTED_FROM_NOTIFICATION : String = "com.brm.brmlabkotlin.services " +
                ".started_from_notification"
    }



    private var calendar = Calendar.getInstance()


    private var mChangingConfiguration: Boolean = false
    private lateinit var mNotificationManager: NotificationManager
    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallBack: LocationCallback
    private lateinit var mServiceHandler: Handler
    private lateinit var mLocation: Location




    private val mBinder = LocalBinder()




    override fun onCreate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallBack = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                onNewLocation(p0?.lastLocation)
            }
        }
        createLocationRequest()
        getLastLocation()

        val handlerThread = HandlerThread("BRMLab")
        handlerThread.start()
        mServiceHandler = Handler(handlerThread.looper)
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val mChannel : NotificationChannel = NotificationChannel(CHANNEL_ID,
            "BRMLab",
            NotificationManager.IMPORTANCE_DEFAULT)
            mNotificationManager.createNotificationChannel(mChannel)
        }

    }

    private fun getLastLocation() {
        try {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                if (it.isSuccessful && it.result != null){
                    mLocation = it.result!!
                }

                else
                    Log.d("MyLog", "Failed to get location")
            }
        }catch (e: SecurityException){
            Log.d("MyLog", e.toString())
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = UPDATE_INTERVAL_IN_MIL
        locationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MIL
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun onNewLocation(lastLocation: Location?) {
        mLocation = lastLocation!!
        EventBus.getDefault().postSticky(SendLocationToActivity(mLocation))

        //Update notification content if running as a foreground service
        if (serviceIsRunningInForeGround(this))
            mNotificationManager.notify(NOTI_ID, getNotification())
    }

    private fun getNotification(): Notification? {
        val intent = Intent(this, BackgroundService::class.java)
        val common = Common()
        val text: String = common.getLocationText(mLocation)

        val mdFormat = SimpleDateFormat("HH:mm:ss")
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)
        val servicePendingIntent = PendingIntent.getService(this, 0, intent,
        PendingIntent.FLAG_UPDATE_CURRENT)
        val activityPendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, TestActivity::class.java), 0)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this)
            .addAction(android.R.drawable.ic_menu_mylocation, "Запустить", activityPendingIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Остановить", servicePendingIntent)
            .setContentText(text)
            .setContentTitle(common.getLocationTitle(this, mdFormat, calendar))
            .setOngoing(true)
            .setPriority(Notification.PRIORITY_HIGH)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setTicker(text)
            .setWhen(System.currentTimeMillis())
        //Set the channel id for Android O

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            builder.setChannelId(CHANNEL_ID)
        }
        return builder.build()
    }

    private fun serviceIsRunningInForeGround(context: Context): Boolean {
        val manager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) if (javaClass.name == service.service.className) if (service.foreground) return true
        return false
    }

    override fun onBind(intent: Intent?): IBinder? {
        stopForeground(true)
        mChangingConfiguration = false

        return mBinder
    }

    inner class LocalBinder : Binder() {
        internal val service: BackgroundService
            get() = this@BackgroundService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val startedFromNotification: Boolean? = intent?.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION, false)
        if (startedFromNotification!!)
        {
            removeLocationUpdates()
            stopSelf()
        }
        return START_NOT_STICKY
    }

    fun removeLocationUpdates() {
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallBack)
            val common = Common()
            common.setRequestLocationUpdates(this, false)
            stopSelf()
        }
        catch (e: SecurityException)
        {val common = Common()
        common.setRequestLocationUpdates(this, true)
        Log.d("MyLog", "Last location permission. Could not remove updates $e")}
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mChangingConfiguration = true
    }

    override fun onRebind(intent: Intent?) {
        stopForeground(true)
        mChangingConfiguration = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        val common = Common()
        if (!mChangingConfiguration && common.requestingLocationUpdates(this))
            startForeground(NOTI_ID, getNotification())
        return true
    }

    override fun onDestroy() {
        mServiceHandler.removeCallbacks(null)
        super.onDestroy()

    }

    fun requestLocationUpdates() {

        Common().setRequestLocationUpdates(this, true)
        startService(Intent(applicationContext, BackgroundService::class.java))
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, Looper.myLooper())
        }
        catch (ex: SecurityException){
            Log.d("MyLog", "Lost location permission, could not request it $ex")
        }
    }
}