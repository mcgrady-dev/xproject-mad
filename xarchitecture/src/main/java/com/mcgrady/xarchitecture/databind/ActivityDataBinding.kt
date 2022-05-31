package com.mcgrady.xarchitecture.databind

import android.app.Activity
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mcgrady.xarchitecture.delegate.ActivityDelegate
import com.mcgrady.xarchitecture.ext.addLifecycleFragment
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2021/7/19.
 */
class ActivityDataBinding<T : ViewDataBinding>(
    val activity: Activity,
    @LayoutRes val resId: Int,
    private var block: (T.() -> Unit)? = null
) : ActivityDelegate<T>(activity) {

    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return binding?.run {
            this
        } ?: let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                activity.addLifecycleFragment { destroyed() }
            }

            val bind: T = DataBindingUtil.setContentView(thisRef, resId)
            return bind.apply {
                if (activity is ComponentActivity) {
                    bind.lifecycleOwner = activity
                }
                binding = this
                block?.invoke(this)
                block = null
            }
        }
    }
}