package com.mcgrady.xproject.common.core.app

import android.app.Application
import com.mcgrady.xproject.common.core.BuildConfig
import com.mcgrady.xproject.common.core.extensions.internalAppCachePath
import com.mcgrady.xproject.common.core.lifecycle.ActivityLifecycleCallbacksImpl
import com.mcgrady.xproject.common.core.log.tree.FileLoggingTree
import com.mcgrady.xproject.common.core.log.tree.ReleaseTree
import timber.log.Timber

/**
 * Created by mcgrady on 2021/12/16.
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
//            val file: Int? = null
            Timber.plant(FileLoggingTree(internalAppCachePath()))
        }

        registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImpl())
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}