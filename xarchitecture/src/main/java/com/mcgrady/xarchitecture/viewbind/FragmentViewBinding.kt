package com.mcgrady.xarchitecture.viewbind

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarchitecture.base.delegate.FragmentDelegate
import com.mcgrady.xarchitecture.ext.bindMethod
import com.mcgrady.xarchitecture.ext.infateMethod
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2021/7/19.
 */
class FragmentViewBinding<T: ViewBinding>(
    bindingClass: Class<T>,
    fragment: Fragment
) : FragmentDelegate<T>(fragment) {

    private val layoutInflater = bindingClass.infateMethod()
    private val bindView = bindingClass.bindMethod()

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        binding?.let { return it }

        val invoke: T
        if (thisRef.view == null) {
            invoke = layoutInflater.invoke(null, thisRef.layoutInflater) as T
        } else {
            invoke = bindView.invoke(null, thisRef.view) as T
        }

        return invoke.also { this.binding = it }
    }
}