package com.mcgrady.xproject.common.core.lifecycle

import android.app.Activity

interface IActivityLifecycle {

    fun registerFragmentCallbacks(activity: Activity)

    fun unregisterFragmentCallbacks(activity: Activity)
}