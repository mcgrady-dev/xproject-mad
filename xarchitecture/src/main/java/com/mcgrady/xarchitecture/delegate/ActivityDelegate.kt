package com.mcgrady.xarchitecture.delegate

import android.app.Activity
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarchitecture.ext.observerWhenDestroyed
import kotlin.properties.ReadOnlyProperty

/**
 * Created by mcgrady on 2021/7/19.
 */
abstract class ActivityDelegate<T : ViewBinding>(activity: Activity) :
    ReadOnlyProperty<Activity, T> {

    protected var binding: T? = null

    init {
        when (activity) {
            is ComponentActivity -> activity.lifecycle.observerWhenDestroyed { destroyed() }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    activity.observerWhenDestroyed { destroyed() }
                }
            }
        }
    }

    protected fun destroyed() {
        binding = null
    }

    companion object {
        const val LIFECYCLE_FRAGMENT_TAG = "com.mcgrady.xarchitecture.lifecycle_fragment"
    }
}