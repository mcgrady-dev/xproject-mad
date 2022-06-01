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
package com.mcgrady.xproject.common.core.base.recycler

import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mcgrady.xproject.common.core.extensions.binding

/**
 * Created by mcgrady on 2022/2/23.
 */
abstract class BindingAdapter<T, BD : ViewDataBinding>(data: MutableList<T>? = null) : RecyclerView.Adapter<BaseViewHolder<BD>>() {

  var data: MutableList<T> = data ?: arrayListOf()
    internal set

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

  protected fun getItem(@IntRange(from = 0) position: Int): T = data[position]

  override fun getItemCount(): Int = data.size

  /*************************** 设置数据相关 ******************************************/

  /**
   * setting up a new instance to data;
   * 设置新的数据实例，替换原有内存引用。
   * 通常情况下，如非必要，请使用[setList]修改内容
   *
   * @param list
   */
  open fun setNewInstance(list: MutableList<T>?) {
    if (list === this.data) {
      return
    }

    this.data = list ?: arrayListOf()
    notifyDataSetChanged()
  }

  /**
   * 使用新的数据集合，改变原有数据集合内容。
   * 注意：不会替换原有的内存引用，只是替换内容
   *
   * @param list Collection<T>?
   */
  open fun setList(list: Collection<T>?) {
    if (list !== this.data) {
      this.data.clear()
      if (!list.isNullOrEmpty()) {
        this.data.addAll(list)
      }
    } else {
      if (!list.isNullOrEmpty()) {
        val newList = ArrayList(list)
        this.data.clear()
        this.data.addAll(newList)
      } else {
        this.data.clear()
      }
    }
    notifyDataSetChanged()
  }

  /**
   * change data
   * 改变某一位置数据
   */
  open fun setData(@IntRange(from = 0) index: Int, data: T) {
    if (index >= this.data.size) {
      return
    }
    this.data[index] = data
    notifyItemChanged(index)
  }

  /**
   * add one new data in to certain location
   * 在指定位置添加一条新数据
   *
   * @param position
   */
  open fun addData(@IntRange(from = 0) position: Int, data: T) {
    this.data.add(position, data)
    notifyItemInserted(position)
  }

  /**
   * add one new data
   * 添加一条新数据
   */
  open fun addData(@NonNull data: T) {
    this.data.add(data)
    notifyItemInserted(this.data.size)
  }

  /**
   * add new data in to certain location
   * 在指定位置添加数据
   *
   * @param position the insert position
   * @param newData  the new data collection
   */
  open fun addData(@IntRange(from = 0) position: Int, newData: Collection<T>) {
    this.data.addAll(position, newData)
    notifyItemRangeInserted(position, newData.size)
  }

  open fun addData(@NonNull newData: Collection<T>) {
    this.data.addAll(newData)
    notifyItemRangeInserted(this.data.size - newData.size, newData.size)
  }

  /**
   * remove the item associated with the specified position of adapter
   * 删除指定位置的数据
   *
   * @param position
   */
  open fun removeAt(@IntRange(from = 0) position: Int) {
    if (position >= data.size) {
      return
    }
    this.data.removeAt(position)
    val internalPosition = position
    notifyItemRemoved(internalPosition)
    notifyItemRangeChanged(internalPosition, this.data.size - internalPosition)
  }

  open fun remove(data: T) {
    val index = this.data.indexOf(data)
    if (index == -1) {
      return
    }
    removeAt(index)
  }
}
