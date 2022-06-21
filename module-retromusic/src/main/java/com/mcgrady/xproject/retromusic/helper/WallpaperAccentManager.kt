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
package com.mcgrady.xproject.retromusic.helper

import android.app.WallpaperManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.theme.ThemeStore
import com.mcgrady.xproject.theme.util.VersionUtils

class WallpaperAccentManager(val context: Context) {

    private val onColorsChangedListener by lazy {
        WallpaperManager.OnColorsChangedListener { _, _ ->
            updateColors()
        }
    }

    fun init() {
        if (VersionUtils.hasOreoMR1()) {
            with(WallpaperManager.getInstance(context)) {
                updateColors()
                if (PreferenceUtil.wallpaperAccent) {
                    addOnColorsChangedListener(
                        onColorsChangedListener,
                        Handler(Looper.getMainLooper())
                    )
                }
            }
        }
    }

    fun release() {
        if (VersionUtils.hasOreoMR1()) {
            WallpaperManager.getInstance(context)
                .removeOnColorsChangedListener(onColorsChangedListener)
        }
    }

    private fun updateColors() {
        if (VersionUtils.hasOreoMR1()) {
            val colors = WallpaperManager.getInstance(context)
                .getWallpaperColors(WallpaperManager.FLAG_SYSTEM)
            if (colors != null) {
                val primaryColor = colors.primaryColor.toArgb()
                ThemeStore.editTheme(context).wallpaperColor(context, primaryColor).commit()
            }
        }
    }
}
