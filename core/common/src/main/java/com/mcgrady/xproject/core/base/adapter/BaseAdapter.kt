package com.mcgrady.xproject.core.base.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.annotation.IntRange

/**
 * Created by mcgrady on 2022/12/2.
 */
abstract class BaseAdapter<T, VH: RecyclerView.ViewHolder>(open var items: List<T> = emptyList()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _recyclerView: RecyclerView? = null
    val recyclerView: RecyclerView
        get() {
            checkNotNull(_recyclerView) {
                "Please get it after onAttachedToRecyclerView()"
            }
            return _recyclerView!!
        }
    protected var onItemClickListener: OnItemClickListener<T>? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        _recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        _recyclerView = null
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return onCreateViewHolder(parent.context, parent, viewType).apply {
            bindViewClickListener(this, viewType)
        }
    }

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder as VH, position, getItem(position))
    }

    final override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        if (payloads.isEmpty()) {
            @Suppress("UNCHECKED_CAST")
            onBindViewHolder(holder as VH, position, getItem(position))
            return
        }

        @Suppress("UNCHECKED_CAST")
        onBindViewHolder(holder as VH, position, getItem(position), payloads)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    protected abstract fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH

    protected abstract fun onBindViewHolder(holder: VH, position: Int, item: T?)

    protected open fun onBindViewHolder(holder: VH, position: Int, item: T?, payloads: List<Any>) {
        onBindViewHolder(holder, position, item)
    }

    protected open fun bindViewClickListener(viewHolder: VH, viewType: Int) {
        onItemClickListener?.let {
            viewHolder.itemView.setOnClickListener { v ->
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }

                it.onClick(this@BaseAdapter, v, position)
            }
        }
    }

    open fun submitList(list: List<T>?) {
        if (list === items) return

        val newList = list ?: emptyList()
        items = newList
        notifyDataSetChanged()
    }

    open fun getItem(@IntRange(from = 0) position: Int): T? = items.getOrNull(position)

    override fun getItemViewType(position: Int): Int {
        return getItemViewType(position, items)
    }

    protected open fun getItemViewType(position: Int, list: List<T>): Int = 0

    fun setOnItemClickListener(listener: OnItemClickListener<T>?) = apply {
        this.onItemClickListener = listener
    }

    fun interface OnItemClickListener<T> {
        fun onClick(adapter: BaseAdapter<T, *>, view: View, position: Int)
    }
}