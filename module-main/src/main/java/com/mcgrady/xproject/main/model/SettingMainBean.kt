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
package com.mcgrady.xproject.main.model

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil

/**
 * Created by mcgrady on 2022/2/17.
 */
data class SettingMainBean(
    val action: Int,
    val name: String,
    val iconRes: Int,
    val desc: String? = null
) {

//    fun hasDesc(): Boolean = !desc.isNullOrEmpty()

  companion object {
    val CALLBACK: DiffUtil.ItemCallback<SettingMainBean> = object : DiffUtil.ItemCallback<SettingMainBean>() {
      override fun areItemsTheSame(
          oldItem: SettingMainBean,
          newItem: SettingMainBean
      ): Boolean = oldItem.name == newItem.name

      override fun areContentsTheSame(
          oldItem: SettingMainBean,
          newItem: SettingMainBean
      ): Boolean = true
    }
  }

  fun itemClick(view: View) {
    view.findNavController().navigate(action)
  }
}
