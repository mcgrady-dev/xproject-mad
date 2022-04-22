package com.mcgrady.xproject.common.core.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.mcgrady.xproject.common.core.extensions.navigationBar
import com.mcgrady.xproject.common.core.extensions.statusBar
import com.mcgrady.xproject.common.core.log.Log

/**
 * Created by mcgrady on 2021/10/27.
 */
open class ActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks, IActivityLifecycle {

    companion object {

        const val TAG = "ActivityLifecycleCallback"
    }

    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityPreCreated(activity, savedInstanceState)

        registerFragmentCallbacks(activity)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.i("$TAG ${activity::class.simpleName}: onCreated, taskId=${activity.taskId}")

        statusBar(activity = activity as FragmentActivity) {
            transparent()
        }
        navigationBar(activity = activity as FragmentActivity) {
            transparent()
        }
    }

    override fun onActivityStarted(activity: Activity) {
        Log.i("$TAG ${activity::class.simpleName}: onStarted")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.i("$TAG ${activity::class.simpleName}: onResumed")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.i("$TAG ${activity::class.simpleName}: onPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.i("$TAG ${activity::class.simpleName}: onStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.i("$TAG ${activity::class.simpleName}: onSaveInstanceState, outState=${outState}")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.i("$TAG ${activity::class.simpleName}: onDestroyed")

        unregisterFragmentCallbacks(activity)
    }

    override fun registerFragmentCallbacks(activity: Activity) {
        Log.i("$TAG ${activity::class.simpleName} registerFragmentLifecycleCallbacks")
        (activity as FragmentActivity).supportFragmentManager.registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacksImpl, true)
    }

    override fun unregisterFragmentCallbacks(activity: Activity) {
        Log.i("$TAG ${activity::class.simpleName} unregisterFragmentLifecycleCallbacks")
        (activity as FragmentActivity).supportFragmentManager.unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacksImpl)
    }
}