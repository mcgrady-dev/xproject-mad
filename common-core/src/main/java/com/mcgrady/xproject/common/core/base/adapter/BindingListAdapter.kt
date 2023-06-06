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
package com.mcgrady.xproject.common.core.base.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.mcgrady.xarch.extension.dataBinding

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
        val binding = parent.dataBinding<BD>(layoutResId, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BD>, position: Int) {
        bind(holder.binding, getItem(position))
    }
}
