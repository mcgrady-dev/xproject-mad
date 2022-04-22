package com.mcgrady.xproject

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.Utils
import com.mcgrady.xproject.common.core.app.BaseApplication
import com.mcgrady.xproject.common.core.log.Log
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by mcgrady on 2021/5/10.
 */
@HiltAndroidApp
class App : BaseApplication(), LifecycleEventObserver {

    override fun onCreate() {
        super.onCreate()

        Utils.init(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> {
                Log.i("LifecycleObserver: 应用回到前台")
            }
            Lifecycle.Event.ON_STOP -> {
                Log.i("LifecycleObserver: 应用退到后台")
            }
            else -> {}
        }
    }
}