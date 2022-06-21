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

import android.app.SearchManager
import android.os.Bundle
import android.provider.MediaStore
import com.mcgrady.xproject.retromusic.model.Song
import com.mcgrady.xproject.retromusic.repository.RealSongRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object SearchQueryHelper : KoinComponent {
    private const val TITLE_SELECTION = "lower(" + MediaStore.Audio.AudioColumns.TITLE + ") = ?"
    private const val ALBUM_SELECTION = "lower(" + MediaStore.Audio.AudioColumns.ALBUM + ") = ?"
    private const val ARTIST_SELECTION = "lower(" + MediaStore.Audio.AudioColumns.ARTIST + ") = ?"
    private const val AND = " AND "
    private val songRepository by inject<RealSongRepository>()
    var songs = ArrayList<Song>()

    @JvmStatic
    fun getSongs(extras: Bundle): List<Song> {
        val query = extras.getString(SearchManager.QUERY, null)
        val artistName = extras.getString(MediaStore.EXTRA_MEDIA_ARTIST, null)
        val albumName = extras.getString(MediaStore.EXTRA_MEDIA_ALBUM, null)
        val titleName = extras.getString(MediaStore.EXTRA_MEDIA_TITLE, null)

        var songs = listOf<Song>()
        if (artistName != null && albumName != null && titleName != null) {
            songs = songRepository.songs(
                songRepository.makeSongCursor(
                    ARTIST_SELECTION + AND + ALBUM_SELECTION + AND + TITLE_SELECTION,
                    arrayOf(
                        artistName.lowercase(),
                        albumName.lowercase(),
                        titleName.lowercase()
                    )
                )
            )
        }
        if (songs.isNotEmpty()) {
            return songs
        }
        if (artistName != null && titleName != null) {
            songs = songRepository.songs(
                songRepository.makeSongCursor(
                    ARTIST_SELECTION + AND + TITLE_SELECTION,
                    arrayOf(
                        artistName.lowercase(),
                        titleName.lowercase()
                    )
                )
            )
        }
        if (songs.isNotEmpty()) {
            return songs
        }
        if (albumName != null && titleName != null) {
            songs = songRepository.songs(
                songRepository.makeSongCursor(
                    ALBUM_SELECTION + AND + TITLE_SELECTION,
                    arrayOf(
                        albumName.lowercase(),
                        titleName.lowercase()
                    )
                )
            )
        }
        if (songs.isNotEmpty()) {
            return songs
        }
        if (artistName != null) {
            songs = songRepository.songs(
                songRepository.makeSongCursor(
                    ARTIST_SELECTION,
                    arrayOf(artistName.lowercase())
                )
            )
        }
        if (songs.isNotEmpty()) {
            return songs
        }
        if (albumName != null) {
            songs = songRepository.songs(
                songRepository.makeSongCursor(
                    ALBUM_SELECTION,
                    arrayOf(albumName.lowercase())
                )
            )
        }
        if (songs.isNotEmpty()) {
            return songs
        }
        if (titleName != null) {
            songs = songRepository.songs(
                songRepository.makeSongCursor(
                    TITLE_SELECTION,
                    arrayOf(titleName.lowercase())
                )
            )
        }
        if (songs.isNotEmpty()) {
            return songs
        }
        songs = songRepository.songs(
            songRepository.makeSongCursor(
                ARTIST_SELECTION,
                arrayOf(query.lowercase())
            )
        )

        if (songs.isNotEmpty()) {
            return songs
        }
        songs = songRepository.songs(
            songRepository.makeSongCursor(
                ALBUM_SELECTION,
                arrayOf(query.lowercase())
            )
        )
        if (songs.isNotEmpty()) {
            return songs
        }
        songs = songRepository.songs(
            songRepository.makeSongCursor(
                TITLE_SELECTION,
                arrayOf(query.lowercase())
            )
        )
        return songs.ifEmpty { ArrayList() }
    }
}
