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
package com.mcgrady.xproject.retromusic.service

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import androidx.core.content.edit
import androidx.core.net.toUri
import com.mcgrady.xproject.retromusic.extensions.albumArtUri
import com.mcgrady.xproject.retromusic.model.Song

class PersistentStorage(context: Context) {

    private val prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun saveSong(song: Song) {
        prefs.edit {
            putLong("song_id", song.id)
            putString("song_title", song.title)
            putString("song_artist", song.artistName)
            putString("song_cover", song.albumArtUri.toString())
        }
    }

    fun recentSong(): MediaBrowserCompat.MediaItem {
        return MediaBrowserCompat.MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(prefs.getLong("song_id", 0L).toString())
                .setTitle(prefs.getString("song_title", ""))
                .setSubtitle(prefs.getString("song_artist", ""))
                .setIconUri(prefs.getString("song_cover", "")?.toUri())
                .build(),
            MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
        )
    }

    companion object {
        const val PREFERENCES_NAME = "retro_recent"

        @Volatile
        private var instance: PersistentStorage? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: PersistentStorage(context).also { instance = it }
            }
    }
}
