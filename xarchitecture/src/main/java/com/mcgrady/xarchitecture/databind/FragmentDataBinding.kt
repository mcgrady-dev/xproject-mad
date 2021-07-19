package com.mcgrady.xarchitecture.databind

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.mcgrady.xarchitecture.base.delegate.FragmentDelegate
import com.mcgrady.xarchitecture.ext.infateMethod
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2021/7/19.
 */
class FragmentDataBinding<T: ViewDataBinding>(
    bindingClass: Class<T>,
    val fragment: Fragment,
    private var block: (T.() -> Unit)? = null
) : FragmentDelegate<T>(fragment) {

    private val layoutInflater = bindingClass.infateMethod()

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {

        binding?.let { return it }

        val bind: T
        if (thisRef.view == null) {
            // 这里为了兼容在 navigation 中使用 Fragment
            bind = layoutInflater.invoke(null, thisRef.layoutInflater) as T
        } else {
            bind = DataBindingUtil.bind(thisRef.view!!)!!
        }

        return bind.also {
            binding = it
            it.lifecycleOwner = fragment.viewLifecycleOwner
            block?.invoke(it)
            block = null
        }
    }
}