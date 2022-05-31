package com.mcgrady.xarchitecture.databind

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarchitecture.ext.addLifecycleFragment
import com.mcgrady.xarchitecture.ext.observerWhenDestroyed
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2022/5/31.
 */
class ViewGroupDataBinding<T : ViewBinding>(
    @Suppress("UNUSED_PARAMETER") bindingClass: Class<T>,
    @LayoutRes val resId: Int,
    val inflater: LayoutInflater,
    val viewGroup: ViewGroup? = null,
    private var block: (T.() -> Unit)? = null
) : ReadOnlyProperty<ViewGroup, T> {

    private var viewBinding: T? = null

    init {
        viewGroup?.apply {
            when (context) {
                is ComponentActivity -> {
                    (context as ComponentActivity?)?.lifecycle?.observerWhenDestroyed { destroyed() }
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
        return viewBinding?.run {
            this
        } ?: let {
            val bind = DataBindingUtil.inflate(inflater, resId, thisRef, true) as T
            val value = block
            bind.apply {
                viewBinding = this
                value?.invoke(this)
                block = null
            }
        }
    }

    private fun destroyed() {
        viewBinding = null
    }
}