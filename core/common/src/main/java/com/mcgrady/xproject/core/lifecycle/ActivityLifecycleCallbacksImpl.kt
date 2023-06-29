/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcgrady.xproject.core.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import timber.log.Timber

/**
 * Created by mcgrady on 2021/10/27.
 */
open class ActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks, IActivityLifecycle {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Timber.tag(TAG).i(" ${activity::class.java.canonicalName}: onCreated, taskId=${activity.taskId}")

        registerFragmentCallbacks(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        Timber.tag(TAG).i(" ${activity::class.java.canonicalName}: onStarted")
    }

    override fun onActivityResumed(activity: Activity) {
        Timber.tag(TAG).i(" ${activity::class.java.canonicalName}: onResumed")
    }

    override fun onActivityPaused(activity: Activity) {
        Timber.tag(TAG).i(" ${activity::class.java.canonicalName}: onPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        Timber.tag(TAG).i(" ${activity::class.java.canonicalName}: onStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Timber.tag(TAG).i(" ${activity::class.java.canonicalName}: onSaveInstanceState outState=$outState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Timber.tag(TAG).i(" ${activity::class.java.canonicalName}: onDestroyed")

        unregisterFragmentCallbacks(activity)
    }

    override fun registerFragmentCallbacks(activity: Activity) {
        if (activity is FragmentActivity) {
            Timber.tag(TAG).i(" ${activity::class.java.canonicalName}: registerFragmentCallbacks")
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacksImpl, true)
        }
    }

    override fun unregisterFragmentCallbacks(activity: Activity) {
        if (activity is FragmentActivity) {
            Timber.tag(TAG).i(" ${activity::class.java.canonicalName}: unregisterFragmentCallbacks")
            activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacksImpl)
        }
    }

    companion object {
        const val TAG = "ActivityLifecycle"
    }
}
