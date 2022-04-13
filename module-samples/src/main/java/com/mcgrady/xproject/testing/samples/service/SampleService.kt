package com.mcgrady.xproject.testing.samples.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import com.mcgrady.xproject.common.core.log.Log
import kotlin.random.Random

class SampleService : Service() {

    companion object {
        const val TAG = "SampleService"
    }

    private var binder: SampleBinder? = SampleBinder()

    /** method for clients  */
    val randomNumber: Int
        get() = Random.nextInt(100)

    override fun onCreate() {
        super.onCreate()

        Log.d("$TAG onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("$TAG onStartCommand: ${intent.toString()} $flags $startId")

//        val mainHandler = Handler(Looper.getMainLooper())
//        mainHandler.post {
            Toast.makeText(applicationContext, "haha ", Toast.LENGTH_LONG).show()
//        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onRebind(intent: Intent?) {
        Log.d("$TAG onRebind: ${intent.toString()}")
        super.onRebind(intent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("$TAG onBind: ${intent.toString()}")
        return binder?.apply {
            SampleBinder()
        } ?: binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("$TAG onUnbind: ${intent.toString()}")
        binder = null
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.d("$TAG onDestroy")
        binder = null
        super.onDestroy()
    }

    inner class SampleBinder : Binder() {

        fun getService(): SampleService = this@SampleService
    }
}