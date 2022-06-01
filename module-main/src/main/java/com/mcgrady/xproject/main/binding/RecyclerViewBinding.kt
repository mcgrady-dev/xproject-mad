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
package com.mcgrady.xproject.main.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by mcgrady on 2022/2/17.
 */
object RecyclerViewBinding {

  @Suppress("UNCHECKED_CAST")
  @JvmStatic
  @BindingAdapter("submitList")
  fun bindSubmitList(view: RecyclerView, itemList: List<Any>?) {
    val adapter = view.adapter as? ListAdapter<Any, *> ?: throw RuntimeException("adapter must be not null")

    itemList?.let {
      adapter.submitList(it)
    }
  }
}
