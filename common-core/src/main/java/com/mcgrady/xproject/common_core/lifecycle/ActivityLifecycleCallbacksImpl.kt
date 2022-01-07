package com.mcgrady.xproject.common_core.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.mcgrady.xproject.common_core.log.Log

/**
 * Created by mcgrady on 2021/10/27.
 */
open class ActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.i("onActivityCreated: $activity , taskId is ${activity.taskId}")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.i("onActivityStarted: ${activity.javaClass.simpleName}")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.i("onActivityResumed: ${activity.javaClass.simpleName}")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.i("onActivityPaused: ${activity.javaClass.simpleName}")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.i("onActivityStopped: ${activity.javaClass.simpleName}")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.i("onActivitySaveInstanceState: ${activity.javaClass.simpleName}")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.i("onActivityDestroyed: ${activity.javaClass.simpleName}")
    }
}