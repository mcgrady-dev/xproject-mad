package com.mcgrady.xproject.common.core.base.recycler

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.Bindable
import androidx.databinding.PropertyChangeRegistry
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2022/2/17.
 */
abstract class BindableListAdapter<T, BD : ViewDataBinding> constructor(
    callback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseViewHolder<BD>>(callback) {

    @get:LayoutRes
    abstract val layoutResId: Int

    abstract fun bind(binding: BD, item: T)
}

class BaseViewHolder<BD: ViewDataBinding>(val binding: BD) : RecyclerView.ViewHolder(binding.root) {

}

interface ItemViewModel {
    @get:LayoutRes
    val layoutId: Int
    val viewType: Int
        get() = 0
}