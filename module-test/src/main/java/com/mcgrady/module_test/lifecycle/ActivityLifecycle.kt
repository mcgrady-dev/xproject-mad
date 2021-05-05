package com.mcgrady.module_test.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

/**
 * Created by mcgrady on 2/1/21.
 */
class ActivityLifecycle: Application.ActivityLifecycleCallbacks {

//    lateinit var fragmentLifecycle: FragmentManager.FragmentLifecycleCallbacks
//
//    constructor(fragmentLifecycle: FragmentManager.FragmentLifecycleCallbacks) {
//        this.fragmentLifecycle = fragmentLifecycle
//    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.i(activity::class.simpleName, "onActivityCreated")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.i(activity::class.simpleName, "onActivityStarted")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.i(activity::class.simpleName, "onActivityResumed")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.i(activity::class.simpleName, "onActivityPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.i(activity::class.simpleName, "onActivityStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.i(activity::class.simpleName, "onActivitySaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.i(activity::class.simpleName, "onActivityDestroyed")
    }


}