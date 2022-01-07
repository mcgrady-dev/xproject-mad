package com.mcgrady.xarchitecture.viewbind

import android.app.Activity
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarchitecture.base.delegate.ActivityDelegate
import com.mcgrady.xarchitecture.ext.infateMethod
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2021/7/14.
 */
class ActivityViewBinding<T : ViewBinding>(
    bindingClass: Class<T>,
    val activity: Activity
) : ActivityDelegate<T>(activity) {

    private var layoutInflater = bindingClass.infateMethod()

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return binding?.run {
            this
        } ?: let {
            addLifecycleFragment(activity)

            //获取 ViewBinding
            val bind = layoutInflater.invoke(null, thisRef.layoutInflater) as T
            thisRef.setContentView(bind.root)
            return bind.apply { binding = this }
        }
    }
}