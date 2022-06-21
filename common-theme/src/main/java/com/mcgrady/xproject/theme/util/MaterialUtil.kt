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

import android.content.res.ColorStateList
import android.graphics.Color
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.mcgrady.xproject.theme.ThemeStore

object MaterialUtil {

    @JvmOverloads
    @JvmStatic
    fun setTint(
        button: MaterialButton,
        background: Boolean = true,
        color: Int = ThemeStore.accentColor(button.context)
    ) {

        button.isAllCaps = false
        val context = button.context
        val colorState = ColorStateList.valueOf(color)
        val textColor =
            ColorStateList.valueOf(
                MaterialValueHelper.getPrimaryTextColor(
                    context,
                    ColorUtil.isColorLight(color)
                )
            )

        if (background) {
            button.backgroundTintList = colorState
            button.setTextColor(textColor)
            button.iconTint = textColor
        } else {
            button.setTextColor(colorState)
            button.iconTint = colorState
        }
    }

    @JvmOverloads
    @JvmStatic
    fun tintColor(
        button: MaterialButton,
        textColor: Int = Color.WHITE,
        backgroundColor: Int = Color.BLACK
    ) {
        val backgroundColorStateList = ColorStateList.valueOf(backgroundColor)
        val textColorColorStateList = ColorStateList.valueOf(textColor)
        button.backgroundTintList = backgroundColorStateList
        button.setTextColor(textColorColorStateList)
        button.iconTint = textColorColorStateList
    }

    @JvmOverloads
    @JvmStatic
    fun setTint(textInputLayout: TextInputLayout, background: Boolean = true) {
        val context = textInputLayout.context
        val accentColor = ThemeStore.accentColor(context)
        val colorState = ColorStateList.valueOf(accentColor)

        if (background) {
            textInputLayout.backgroundTintList = colorState
            textInputLayout.defaultHintTextColor = colorState
        } else {
            textInputLayout.boxStrokeColor = accentColor
            textInputLayout.defaultHintTextColor = colorState
            textInputLayout.isHintAnimationEnabled = true
        }
    }
}
