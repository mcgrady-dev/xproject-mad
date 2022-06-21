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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import com.mcgrady.xproject.retromusic.appshortcuts.shortcuttype.LastAddedShortcutType
import com.mcgrady.xproject.retromusic.appshortcuts.shortcuttype.ShuffleAllShortcutType
import com.mcgrady.xproject.retromusic.appshortcuts.shortcuttype.TopTracksShortcutType
import com.mcgrady.xproject.retromusic.extensions.extraNotNull
import com.mcgrady.xproject.retromusic.model.Playlist
import com.mcgrady.xproject.retromusic.model.smartplaylist.LastAddedPlaylist
import com.mcgrady.xproject.retromusic.model.smartplaylist.ShuffleAllPlaylist
import com.mcgrady.xproject.retromusic.model.smartplaylist.TopTracksPlaylist
import com.mcgrady.xproject.retromusic.service.MusicService
import com.mcgrady.xproject.retromusic.service.MusicService.Companion.ACTION_PLAY_PLAYLIST
import com.mcgrady.xproject.retromusic.service.MusicService.Companion.INTENT_EXTRA_PLAYLIST
import com.mcgrady.xproject.retromusic.service.MusicService.Companion.INTENT_EXTRA_SHUFFLE_MODE
import com.mcgrady.xproject.retromusic.service.MusicService.Companion.SHUFFLE_MODE_NONE
import com.mcgrady.xproject.retromusic.service.MusicService.Companion.SHUFFLE_MODE_SHUFFLE

class AppShortcutLauncherActivity : Activity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (extraNotNull(KEY_SHORTCUT_TYPE, SHORTCUT_TYPE_NONE).value) {
            SHORTCUT_TYPE_SHUFFLE_ALL -> {
                startServiceWithPlaylist(
                    SHUFFLE_MODE_SHUFFLE, ShuffleAllPlaylist()
                )
                DynamicShortcutManager.reportShortcutUsed(this, ShuffleAllShortcutType.id)
            }
            SHORTCUT_TYPE_TOP_TRACKS -> {
                startServiceWithPlaylist(
                    SHUFFLE_MODE_NONE, TopTracksPlaylist()
                )
                DynamicShortcutManager.reportShortcutUsed(this, TopTracksShortcutType.id)
            }
            SHORTCUT_TYPE_LAST_ADDED -> {
                startServiceWithPlaylist(
                    SHUFFLE_MODE_NONE, LastAddedPlaylist()
                )
                DynamicShortcutManager.reportShortcutUsed(this, LastAddedShortcutType.id)
            }
        }
        finish()
    }

    private fun startServiceWithPlaylist(shuffleMode: Int, playlist: Playlist) {
        val intent = Intent(this, MusicService::class.java)
        intent.action = ACTION_PLAY_PLAYLIST

        val bundle = bundleOf(
            INTENT_EXTRA_PLAYLIST to playlist,
            INTENT_EXTRA_SHUFFLE_MODE to shuffleMode
        )

        intent.putExtras(bundle)

        startService(intent)
    }

    companion object {
        const val KEY_SHORTCUT_TYPE = "com.mcgrady.xproject.retromusic.appshortcuts.ShortcutType"
        const val SHORTCUT_TYPE_SHUFFLE_ALL = 0L
        const val SHORTCUT_TYPE_TOP_TRACKS = 1L
        const val SHORTCUT_TYPE_LAST_ADDED = 2L
        const val SHORTCUT_TYPE_NONE = 4L
    }
}
