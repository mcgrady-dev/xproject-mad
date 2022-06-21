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
package com.mcgrady.xproject.retromusic.appshortcuts.shortcuttype

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.ShortcutInfo
import android.os.Build
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.appshortcuts.AppShortcutIconGenerator
import com.mcgrady.xproject.retromusic.appshortcuts.AppShortcutLauncherActivity

@TargetApi(Build.VERSION_CODES.N_MR1)
class LastAddedShortcutType(context: Context) : BaseShortcutType(context) {

    override val shortcutInfo: ShortcutInfo
        get() = ShortcutInfo.Builder(
            context,
            id
        ).setShortLabel(context.getString(R.string.app_shortcut_last_added_short)).setLongLabel(
            context.getString(R.string.app_shortcut_last_added_long)
        ).setIcon(
            AppShortcutIconGenerator.generateThemedIcon(
                context,
                R.drawable.ic_app_shortcut_last_added
            )
        ).setIntent(getPlaySongsIntent(AppShortcutLauncherActivity.SHORTCUT_TYPE_LAST_ADDED))
            .build()

    companion object {

        val id: String
            get() = ID_PREFIX + "last_added"
    }
}
