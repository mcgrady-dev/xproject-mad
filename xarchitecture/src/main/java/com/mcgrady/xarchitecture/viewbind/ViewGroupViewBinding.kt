package com.mcgrady.xarchitecture.viewbind

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarchitecture.ext.addLifecycleFragment
import com.mcgrady.xarchitecture.ext.inflateMethod
import com.mcgrady.xarchitecture.ext.inflateMethodWithViewGroup
import com.mcgrady.xarchitecture.ext.observerWhenDestroyed
import java.lang.reflect.Method
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2021/12/28.
 */
class ViewGroupViewBinding<T : ViewBinding>(
    bindingClass: Class<T>,
    private val inflater: LayoutInflater,
    private val viewGroup: ViewGroup? = null
) : ReadOnlyProperty<ViewGroup, T> {

    private var binding: T? = null
    private var layoutInflater: Method = if (viewGroup != null) {
        bindingClass.inflateMethodWithViewGroup()
    } else {
        bindingClass.inflateMethod()
    }

    init {
        viewGroup?.apply {
            when (context) {
                is ComponentActivity -> {
                    (context as ComponentActivity).lifecycle.observerWhenDestroyed { destroyed() }
                }
                is Activity -> {
                    val activity = context as Activity
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        activity.observerWhenDestroyed { destroyed() }
                    } else {
                        activity.addLifecycleFragment { destroyed() }
                    }
                }
            }
        }
    }

    override fun getValue(thisRef: ViewGroup, property: KProperty<*>): T {
        return binding?.run {
            this
        } ?: let {
            @Suppress("UNCHECKED_CAST")
            val bind: T = viewGroup?.let {
                layoutInflater.invoke(null, inflater, viewGroup) as T
            } ?: let {
                layoutInflater.invoke(null, inflater) as T
            }

            bind.apply {
                if (viewGroup == null) {
                    thisRef.addView(bind.root)
                }
                binding = this
            }
        }
    }

    private fun destroyed() {
        binding = null
    }
}