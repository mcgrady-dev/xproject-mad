package com.mcgrady.xproject.ui.main

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcgrady.xproject.core.base.adapter.BaseAdapter
import com.mcgrady.xproject.core.ui.drawable.TextDrawable
import com.mcgrady.xproject.core.utils.XUtils
import com.mcgrady.xproject.data.entity.MainItemEntity
import com.mcgrady.xproject.databinding.ItemMainBinding

/**
 * Created by mcgrady on 2022/11/24.
 */
class MainItemAdapter : BaseAdapter<MainItemEntity, MainItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: MainItemEntity?) {
        item?.let {
            holder.bindTo(it)
        }
    }

    inner class ViewHolder(private val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: MainItemEntity) {
            val drawable = TextDrawable.builder()
                .beginConfig()
                .bold()
                .textColor(Color.WHITE)
                .toUpperCase()
                .endConfig()
                .buildRound(item.drawableText ?: item.name.first().toString(), XUtils.getRandomColor(false))

            binding.ivImage.setImageDrawable(drawable)
            binding.tvName.text = item.name
        }
    }
}