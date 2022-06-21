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
import androidx.annotation.ColorInt
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.mcgrady.xproject.theme.ThemeStore

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
object NavigationViewUtil {

    fun setItemIconColors(navigationView: NavigationView, @ColorInt normalColor: Int, @ColorInt selectedColor: Int) {
        val iconSl = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ),
            intArrayOf(normalColor, selectedColor)
        )
        navigationView.itemIconTintList = iconSl
        val drawable = navigationView.itemBackground
        navigationView.itemBackground = TintHelper.createTintedDrawable(
            drawable,
            ColorUtil.withAlpha(ThemeStore.accentColor(navigationView.context), 0.2f)
        )
    }

    fun setItemTextColors(navigationView: NavigationView, @ColorInt normalColor: Int, @ColorInt selectedColor: Int) {
        val textSl = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
            intArrayOf(normalColor, selectedColor)
        )
        navigationView.itemTextColor = textSl
    }

    fun setItemIconColors(bottomNavigationView: BottomNavigationView, @ColorInt normalColor: Int, @ColorInt selectedColor: Int) {
        val iconSl = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
            intArrayOf(normalColor, selectedColor)
        )
        bottomNavigationView.itemIconTintList = iconSl
    }

    fun setItemTextColors(bottomNavigationView: BottomNavigationView, @ColorInt normalColor: Int, @ColorInt selectedColor: Int) {
        val textSl = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
            intArrayOf(normalColor, selectedColor)
        )
        bottomNavigationView.itemTextColor = textSl
    }
}
