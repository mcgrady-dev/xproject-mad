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
package com.mcgrady.xproject.retromusic.repository

import android.database.Cursor
import android.provider.MediaStore
import com.mcgrady.xproject.retromusic.model.Album
import com.mcgrady.xproject.retromusic.model.Artist
import com.mcgrady.xproject.retromusic.model.Song
import com.mcgrady.xproject.retromusic.util.PreferenceUtil

/**
 * Created by hemanths on 16/08/17.
 */
interface LastAddedRepository {
    fun recentSongs(): List<Song>

    fun recentAlbums(): List<Album>

    fun recentArtists(): List<Artist>
}

class RealLastAddedRepository(
    private val songRepository: RealSongRepository,
    private val albumRepository: RealAlbumRepository,
    private val artistRepository: RealArtistRepository
) : LastAddedRepository {
    override fun recentSongs(): List<Song> {
        return songRepository.songs(makeLastAddedCursor())
    }

    override fun recentAlbums(): List<Album> {
        return albumRepository.splitIntoAlbums(recentSongs(), sorted = false)
    }

    override fun recentArtists(): List<Artist> {
        return artistRepository.splitIntoArtists(recentAlbums())
    }

    private fun makeLastAddedCursor(): Cursor? {
        val cutoff = PreferenceUtil.lastAddedCutoff
        return songRepository.makeSongCursor(
            MediaStore.Audio.Media.DATE_ADDED + ">?",
            arrayOf(cutoff.toString()),
            MediaStore.Audio.Media.DATE_ADDED + " DESC"
        )
    }
}
