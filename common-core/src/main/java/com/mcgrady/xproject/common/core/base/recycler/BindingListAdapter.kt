package com.mcgrady.xproject.common.core.base.recycler

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.mcgrady.xproject.common.core.extensions.binding

/**
 * Created by mcgrady on 2022/2/17.
 */
abstract class BindingListAdapter<T, BD : ViewDataBinding> constructor(
    callback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseViewHolder<BD>>(callback) {

    @get:LayoutRes
    abstract val layoutResId: Int

    abstract fun bind(binding: BD, item: T)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BD> {
        val binding = parent.binding<BD>(layoutResId, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BD>, position: Int) {
        bind(holder.binding, getItem(position))
    }
}