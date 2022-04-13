package com.mcgrady.xproject.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by mcgrady on 2022/2/17.
 */
object RecyclerViewBinding {

    @JvmStatic
    @BindingAdapter("submitList")
    fun bindSubmitList(view: RecyclerView, itemList: List<Any>?) {
        val adapter = view.adapter as? ListAdapter<Any, *> ?: throw RuntimeException("adapter must be not null")

        itemList?.let {
            adapter.submitList(it)
        }
    }
}