package com.mcgrady.xarchitecture.databind

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2022/1/7.
 */

class ViewHolderDataBinding<T : ViewDataBinding>(
    @Suppress("UNUSED_PARAMETER") bindingClass: Class<T>,
    private var block: (T.() -> Unit)? = null
) : ReadOnlyProperty<RecyclerView.ViewHolder, T> {

    private var binding: T? = null

    override fun getValue(thisRef: RecyclerView.ViewHolder, property: KProperty<*>): T {
        return binding?.run {
            this
        } ?: let {
            val bind = DataBindingUtil.bind<T>(thisRef.itemView) as T
            val value = block
            bind.apply {
                binding = this
                value?.invoke(this)
                block = null
            }
        }
    }

    private fun destroyed() {
        binding = null
    }
}