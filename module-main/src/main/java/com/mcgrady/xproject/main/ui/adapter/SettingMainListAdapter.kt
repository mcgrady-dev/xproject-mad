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
package com.mcgrady.xproject.main.ui.adapter

import android.view.ViewGroup
import com.mcgrady.xproject.common.core.base.recycler.BaseViewHolder
import com.mcgrady.xproject.common.core.base.recycler.BindingListAdapter
import com.mcgrady.xproject.main.R
import com.mcgrady.xproject.main.databinding.RecyclerItemSettingMainBinding
import com.mcgrady.xproject.main.model.SettingMainBean
import com.mcgrady.xproject.main.util.SettingHandlers

/**
 * Created by mcgrady on 2022/2/17.
 */
class SettingMainListAdapter :
  BindingListAdapter<SettingMainBean, RecyclerItemSettingMainBinding>(SettingMainBean.CALLBACK) {

  override val layoutResId: Int
    get() = R.layout.recycler_item_setting_main

  override fun onCreateViewHolder(
      parent: ViewGroup,
      viewType: Int
  ): BaseViewHolder<RecyclerItemSettingMainBinding> {
    return super.onCreateViewHolder(parent, viewType)
  }

  override fun bind(binding: RecyclerItemSettingMainBinding, item: SettingMainBean) {
    binding.apply {
      model = item
      handler = SettingHandlers()
      executePendingBindings()
    }
  }
}
