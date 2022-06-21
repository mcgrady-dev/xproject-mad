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

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore.Audio.AudioColumns
import android.provider.MediaStore.Audio.Playlists.Members
import com.mcgrady.xproject.retromusic.Constants
import com.mcgrady.xproject.retromusic.Constants.IS_MUSIC
import com.mcgrady.xproject.retromusic.extensions.getInt
import com.mcgrady.xproject.retromusic.extensions.getLong
import com.mcgrady.xproject.retromusic.extensions.getString
import com.mcgrady.xproject.retromusic.extensions.getStringOrNull
import com.mcgrady.xproject.retromusic.model.PlaylistSong
import com.mcgrady.xproject.retromusic.model.Song

/**
 * Created by hemanths on 16/08/17.
 */
@Suppress("Deprecation")
object PlaylistSongsLoader {

    @JvmStatic
    fun getPlaylistSongList(context: Context, playlistId: Long): List<Song> {
        val songs = mutableListOf<Song>()
        val cursor =
            makePlaylistSongCursor(
                context,
                playlistId
            )

        if (cursor != null && cursor.moveToFirst()) {
            do {
                songs.add(
                    getPlaylistSongFromCursorImpl(
                        cursor,
                        playlistId
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return songs
    }

    // TODO duplicated in [PlaylistRepository.kt]
    private fun getPlaylistSongFromCursorImpl(cursor: Cursor, playlistId: Long): PlaylistSong {
        val id = cursor.getLong(Members.AUDIO_ID)
        val title = cursor.getString(AudioColumns.TITLE)
        val trackNumber = cursor.getInt(AudioColumns.TRACK)
        val year = cursor.getInt(AudioColumns.YEAR)
        val duration = cursor.getLong(AudioColumns.DURATION)
        val data = cursor.getString(Constants.DATA)
        val dateModified = cursor.getLong(AudioColumns.DATE_MODIFIED)
        val albumId = cursor.getLong(AudioColumns.ALBUM_ID)
        val albumName = cursor.getString(AudioColumns.ALBUM)
        val artistId = cursor.getLong(AudioColumns.ARTIST_ID)
        val artistName = cursor.getString(AudioColumns.ARTIST)
        val idInPlaylist = cursor.getLong(Members._ID)
        val composer = cursor.getStringOrNull(AudioColumns.COMPOSER)
        val albumArtist = cursor.getStringOrNull("album_artist")
        return PlaylistSong(
            id,
            title,
            trackNumber,
            year,
            duration,
            data,
            dateModified,
            albumId,
            albumName,
            artistId,
            artistName,
            playlistId,
            idInPlaylist,
            composer,
            albumArtist
        )
    }

    private fun makePlaylistSongCursor(context: Context, playlistId: Long): Cursor? {
        try {
            return context.contentResolver.query(
                Members.getContentUri("external", playlistId),
                arrayOf(
                    Members.AUDIO_ID, // 0
                    AudioColumns.TITLE, // 1
                    AudioColumns.TRACK, // 2
                    AudioColumns.YEAR, // 3
                    AudioColumns.DURATION, // 4
                    Constants.DATA, // 5
                    AudioColumns.DATE_MODIFIED, // 6
                    AudioColumns.ALBUM_ID, // 7
                    AudioColumns.ALBUM, // 8
                    AudioColumns.ARTIST_ID, // 9
                    AudioColumns.ARTIST, // 10
                    Members._ID, // 11
                    AudioColumns.COMPOSER, // 12
                    "album_artist" // 13
                ),
                IS_MUSIC, null, Members.DEFAULT_SORT_ORDER
            )
        } catch (e: SecurityException) {
            return null
        }
    }
}
