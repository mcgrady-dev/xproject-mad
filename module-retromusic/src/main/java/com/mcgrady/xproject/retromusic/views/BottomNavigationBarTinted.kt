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
package com.mcgrady.xproject.retromusic.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mcgrady.xproject.retromusic.extensions.addAlpha
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.theme.ThemeStore
import com.mcgrady.xproject.theme.util.ATHUtil
import com.mcgrady.xproject.theme.util.ColorUtil
import com.mcgrady.xproject.theme.util.NavigationViewUtil
import dev.chrisbanes.insetter.applyInsetter

class BottomNavigationBarTinted @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : BottomNavigationView(context, attrs, defStyleAttr) {

    init {
        if (!isInEditMode) {
            // If we are in Immersive mode we have to just set empty OnApplyWindowInsetsListener as
            // bottom, start, and end padding is always applied (with the help of OnApplyWindowInsetsListener) to
            // BottomNavigationView to dodge the system navigation bar (so we basically clear that listener).
            if (PreferenceUtil.isFullScreenMode) {
                setOnApplyWindowInsetsListener { _, insets ->
                    insets
                }
            } else {
                applyInsetter {
                    type(navigationBars = true) {
                        padding(vertical = true)
                        margin(horizontal = true)
                    }
                }
            }

            labelVisibilityMode = PreferenceUtil.tabTitleMode

            if (!PreferenceUtil.materialYou) {
                val iconColor = ATHUtil.resolveColor(context, android.R.attr.colorControlNormal)
                val accentColor = ThemeStore.accentColor(context)
                NavigationViewUtil.setItemIconColors(
                    this,
                    ColorUtil.withAlpha(iconColor, 0.5f),
                    accentColor
                )
                NavigationViewUtil.setItemTextColors(
                    this,
                    ColorUtil.withAlpha(iconColor, 0.5f),
                    accentColor
                )
                itemRippleColor = ColorStateList.valueOf(accentColor.addAlpha(0.08F))
                itemActiveIndicatorColor = ColorStateList.valueOf(accentColor.addAlpha(0.12F))
            }
        }
    }
}
