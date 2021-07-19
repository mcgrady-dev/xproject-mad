package com.mcgrady.xproject

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by mcgrady on 2021/5/10.
 */
@HiltAndroidApp
class App : Application(), LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() {
        Log.i("LifecycleObserver", "应用回到前台")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackgorund() {
        Log.i("LifecycleObserver", "应用退到后台")
    }
}