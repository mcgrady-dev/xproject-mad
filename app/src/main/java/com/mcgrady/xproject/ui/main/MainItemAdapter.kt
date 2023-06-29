/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        viewType: Int,
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
