package com.mcgrady.xproject

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.blankj.utilcode.util.Utils
import com.mcgrady.xproject.common.core.app.BaseApplication
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by mcgrady on 2021/5/10.
 */
@HiltAndroidApp
class App : BaseApplication(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()

        Utils.init(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() {
        Log.i("LifecycleObserver", "应用回到前台")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackgorund() {
        Log.i("LifecycleObserver", "应用退到后台")
    }
}