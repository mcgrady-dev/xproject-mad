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
package com.mcgrady.xproject.retromusic.appshortcuts

import android.content.Context
import android.graphics.drawable.Icon
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.TypedValue
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.extensions.getTintedDrawable
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.theme.ThemeStore

@RequiresApi(Build.VERSION_CODES.N_MR1)
object AppShortcutIconGenerator {
    fun generateThemedIcon(context: Context, iconId: Int): Icon {
        return if (PreferenceUtil.isColoredAppShortcuts) {
            generateUserThemedIcon(context, iconId)
        } else {
            generateDefaultThemedIcon(context, iconId)
        }
    }

    private fun generateDefaultThemedIcon(context: Context, iconId: Int): Icon {
        // Return an Icon of iconId with default colors
        return generateThemedIcon(
            context,
            iconId,
            context.getColor(R.color.app_shortcut_default_foreground),
            context.getColor(R.color.app_shortcut_default_background)
        )
    }

    private fun generateUserThemedIcon(context: Context, iconId: Int): Icon {
        // Get background color from context's theme
        val typedColorBackground = TypedValue()
        context.theme.resolveAttribute(android.R.attr.colorBackground, typedColorBackground, true)

        // Return an Icon of iconId with those colors
        return generateThemedIcon(
            context, iconId, ThemeStore.accentColor(context), typedColorBackground.data
        )
    }

    private fun generateThemedIcon(
        context: Context,
        iconId: Int,
        foregroundColor: Int,
        backgroundColor: Int,
    ): Icon {
        // Get and tint foreground and background drawables
        val vectorDrawable = context.getTintedDrawable(iconId, foregroundColor)
        val backgroundDrawable =
            context.getTintedDrawable(R.drawable.ic_app_shortcut_background, backgroundColor)

        // Squash the two drawables together
        val layerDrawable = LayerDrawable(arrayOf(backgroundDrawable, vectorDrawable))

        // Return as an Icon
        return Icon.createWithBitmap(layerDrawable.toBitmap())
    }
}
