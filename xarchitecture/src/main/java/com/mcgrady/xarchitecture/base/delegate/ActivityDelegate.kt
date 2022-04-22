package com.mcgrady.xarchitecture.base.delegate

import android.app.Activity
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarchitecture.ext.LifecycleFragment
import com.mcgrady.xarchitecture.ext.observerWhenDestroyed
import kotlin.properties.ReadOnlyProperty

/**
 * Created by mcgrady on 2021/7/19.
 */
abstract class ActivityDelegate<T : ViewBinding>(activity: Activity) :
    ReadOnlyProperty<Activity, T> {

    protected var binding: T? = null
    private val LIFECYCLE_FRAGMENT_TAG = "com.mcgrady.xarchitecture.lifecycle_fragment"

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

    @Suppress("DEPRECATION")
    fun addLifecycleFragment(activity: Activity) {
        if (activity is FragmentActivity || activity is AppCompatActivity) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) return

        with(activity.fragmentManager) {
            if (findFragmentByTag(LIFECYCLE_FRAGMENT_TAG) == null) {
                beginTransaction()
                    .add(LifecycleFragment { destroyed() }, LIFECYCLE_FRAGMENT_TAG)
                    .commit()
                executePendingTransactions()
            }
        }
    }

    private fun destroyed() {
        binding = null
    }
}