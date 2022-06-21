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
package com.mcgrady.xproject.retromusic.model

import android.content.Context
import android.os.Parcelable
import com.mcgrady.xproject.retromusic.repository.RealPlaylistRepository
import com.mcgrady.xproject.retromusic.util.MusicUtil
import kotlinx.parcelize.Parcelize
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@Parcelize
open class Playlist(
    val id: Long,
    val name: String
) : Parcelable, KoinComponent {

    companion object {
        val empty = Playlist(-1, "")
    }

    // this default implementation covers static playlists
    fun getSongs(): List<Song> {
        return RealPlaylistRepository(get()).playlistSongs(id)
    }

    open fun getInfoString(context: Context): String {
        val songCount = getSongs().size
        val songCountString = MusicUtil.getSongCountString(context, songCount)
        return MusicUtil.buildInfoString(
            songCountString,
            ""
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Playlist

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
