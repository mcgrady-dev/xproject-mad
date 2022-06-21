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
package com.mcgrady.xproject.theme.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.mcgrady.xproject.theme.R

object MaterialValueHelper {

    @SuppressLint("PrivateResource")
    @JvmStatic
    @ColorInt
    fun getPrimaryTextColor(context: Context?, dark: Boolean): Int {
        return if (dark) {
            ContextCompat.getColor(context!!, R.color.primary_text_default_material_light)
        } else ContextCompat.getColor(context!!, R.color.primary_text_default_material_dark)
    }

    @SuppressLint("PrivateResource")
    @JvmStatic
    @ColorInt
    fun getSecondaryTextColor(context: Context?, dark: Boolean): Int {
        return if (dark) {
            ContextCompat.getColor(context!!, R.color.secondary_text_default_material_light)
        } else ContextCompat.getColor(context!!, R.color.secondary_text_default_material_dark)
    }

    @SuppressLint("PrivateResource")
    @JvmStatic
    @ColorInt
    fun getPrimaryDisabledTextColor(context: Context?, dark: Boolean): Int {
        return if (dark) {
            ContextCompat.getColor(context!!, R.color.primary_text_disabled_material_light)
        } else ContextCompat.getColor(context!!, R.color.primary_text_disabled_material_dark)
    }

    @SuppressLint("PrivateResource")
    @JvmStatic
    @ColorInt
    fun getSecondaryDisabledTextColor(context: Context?, dark: Boolean): Int {
        return if (dark) {
            ContextCompat.getColor(context!!, R.color.secondary_text_disabled_material_light)
        } else ContextCompat.getColor(context!!, R.color.secondary_text_disabled_material_dark)
    }
}
