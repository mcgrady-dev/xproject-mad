package com.mcgrady.xproject

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.mcgrady.xproject.common.core.app.BaseApplication
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by mcgrady on 2021/5/10.
 */
@HiltAndroidApp
class App : BaseApplication(), LifecycleEventObserver {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> {
                Timber.i("LifecycleObserver: 应用回到前台")
            }
            Lifecycle.Event.ON_STOP -> {
                Timber.i("LifecycleObserver: 应用退到后台")
            }
            else -> {}
        }
    }
}