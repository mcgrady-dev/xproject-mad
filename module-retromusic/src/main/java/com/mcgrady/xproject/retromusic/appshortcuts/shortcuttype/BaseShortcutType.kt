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
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.os.Build
import androidx.core.os.bundleOf
import com.mcgrady.xproject.retromusic.appshortcuts.AppShortcutLauncherActivity

@TargetApi(Build.VERSION_CODES.N_MR1)
abstract class BaseShortcutType(internal var context: Context) {

    internal abstract val shortcutInfo: ShortcutInfo

    /**
     * Creates an Intent that will launch MainActivtiy and immediately play {@param songs} in either shuffle or normal mode
     *
     * @param shortcutType Describes the type of shortcut to create (ShuffleAll, TopTracks, custom playlist, etc.)
     * @return
     */
    internal fun getPlaySongsIntent(shortcutType: Long): Intent {
        val intent = Intent(context, AppShortcutLauncherActivity::class.java)
        intent.action = Intent.ACTION_VIEW
        val b = bundleOf(AppShortcutLauncherActivity.KEY_SHORTCUT_TYPE to shortcutType)
        intent.putExtras(b)
        return intent
    }

    companion object {
        internal const val ID_PREFIX = "com.mcgrady.xproject.retromusic.appshortcuts.id."
        val id: String
            get() = ID_PREFIX + "invalid"
    }
}
