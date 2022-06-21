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

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.core.content.getSystemService
import com.mcgrady.xproject.retromusic.appshortcuts.shortcuttype.LastAddedShortcutType
import com.mcgrady.xproject.retromusic.appshortcuts.shortcuttype.ShuffleAllShortcutType
import com.mcgrady.xproject.retromusic.appshortcuts.shortcuttype.TopTracksShortcutType

@TargetApi(Build.VERSION_CODES.N_MR1)
class DynamicShortcutManager(private val context: Context) {
    private val shortcutManager: ShortcutManager? =
        this.context.getSystemService()

    private val defaultShortcuts: List<ShortcutInfo>
        get() = listOf(
            ShuffleAllShortcutType(context).shortcutInfo,
            TopTracksShortcutType(context).shortcutInfo,
            LastAddedShortcutType(context).shortcutInfo
        )

    fun initDynamicShortcuts() {
        // if (shortcutManager.dynamicShortcuts.size == 0) {
        shortcutManager?.dynamicShortcuts = defaultShortcuts
        // }
    }

    fun updateDynamicShortcuts() {
        shortcutManager?.updateShortcuts(defaultShortcuts)
    }

    companion object {

        fun createShortcut(
            context: Context,
            id: String,
            shortLabel: String,
            longLabel: String,
            icon: Icon,
            intent: Intent
        ): ShortcutInfo {
            return ShortcutInfo.Builder(context, id)
                .setShortLabel(shortLabel)
                .setLongLabel(longLabel)
                .setIcon(icon)
                .setIntent(intent)
                .build()
        }

        fun reportShortcutUsed(context: Context, shortcutId: String) {
            context.getSystemService<ShortcutManager>()?.reportShortcutUsed(shortcutId)
        }
    }
}
