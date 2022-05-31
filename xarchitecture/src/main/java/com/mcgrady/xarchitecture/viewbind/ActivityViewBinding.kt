package com.mcgrady.xarchitecture.viewbind

import android.app.Activity
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarchitecture.delegate.ActivityDelegate
import com.mcgrady.xarchitecture.ext.addLifecycleFragment
import com.mcgrady.xarchitecture.ext.inflateMethod
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2021/7/14.
 */
class ActivityViewBinding<T : ViewBinding>(
    bindingClass: Class<T>,
    val activity: Activity
) : ActivityDelegate<T>(activity) {

    private var layoutInflater = bindingClass.inflateMethod()

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return binding?.run {
            this
        } ?: let {
            activity.addLifecycleFragment { destroyed() }

            val bind = layoutInflater.invoke(null, thisRef.layoutInflater) as T
            thisRef.setContentView(bind.root)
            return bind.apply { binding = this }
        }
    }
}