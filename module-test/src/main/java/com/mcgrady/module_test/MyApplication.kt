package com.mcgrady.module_test

import android.app.Application
import com.mcgrady.module_test.lifecycle.ActivityLifecycle

/**
 * Created by mcgrady on 2/1/21.
 */
class MyApplication : Application() {

    lateinit var activityLifecycle: ActivityLifecycle

    override fun onCreate() {
        super.onCreate()

        activityLifecycle = ActivityLifecycle()
        registerActivityLifecycleCallbacks(activityLifecycle)
    }

    override fun onTerminate() {
        super.onTerminate()
        
        unregisterActivityLifecycleCallbacks(activityLifecycle)
    }
}