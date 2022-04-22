package com.mcgrady.xproject.common.core.app

import android.app.Application
import com.mcgrady.xproject.common.core.lifecycle.ActivityLifecycleCallbacksImpl
import com.mcgrady.xproject.common.core.log.Log

/**
 * Created by mcgrady on 2021/12/16.
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Log.init(this)

        registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImpl())
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}