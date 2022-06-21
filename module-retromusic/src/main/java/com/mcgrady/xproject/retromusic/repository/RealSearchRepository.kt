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
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.db.PlaylistWithSongs
import com.mcgrady.xproject.retromusic.fragments.search.Filter
import com.mcgrady.xproject.retromusic.model.Album
import com.mcgrady.xproject.retromusic.model.Artist
import com.mcgrady.xproject.retromusic.model.Genre
import com.mcgrady.xproject.retromusic.model.Song

class RealSearchRepository(
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository,
    private val roomRepository: RoomRepository,
    private val genreRepository: GenreRepository,
) {
    suspend fun searchAll(context: Context, query: String?, filter: Filter): MutableList<Any> {
        val results = mutableListOf<Any>()
        if (query.isNullOrEmpty()) return results
        query.let { searchString ->

            /** Songs **/
            val songs: List<Song> = if (filter == Filter.SONGS || filter == Filter.NO_FILTER) {
                songRepository.songs(searchString)
            } else {
                emptyList()
            }
            if (songs.isNotEmpty()) {
                results.add(context.resources.getString(R.string.songs))
                results.addAll(songs)
            }

            /** Artists **/
            val artists: List<Artist> =
                if (filter == Filter.ARTISTS || filter == Filter.NO_FILTER) {
                    artistRepository.artists(searchString)
                } else {
                    emptyList()
                }
            if (artists.isNotEmpty()) {
                results.add(context.resources.getString(R.string.artists))
                results.addAll(artists)
            }

            /** Albums **/
            val albums: List<Album> = if (filter == Filter.ALBUMS || filter == Filter.NO_FILTER) {
                albumRepository.albums(searchString)
            } else {
                emptyList()
            }
            if (albums.isNotEmpty()) {
                results.add(context.resources.getString(R.string.albums))
                results.addAll(albums)
            }

            /** Album-Artists **/
            val albumArtists: List<Artist> =
                if (filter == Filter.ALBUM_ARTISTS || filter == Filter.NO_FILTER) {
                    artistRepository.albumArtists(searchString)
                } else {
                    emptyList()
                }
            if (albumArtists.isNotEmpty()) {
                results.add(context.resources.getString(R.string.album_artist))
                results.addAll(albumArtists)
            }

            /** Genres **/
            val genres: List<Genre> = if (filter == Filter.GENRES || filter == Filter.NO_FILTER) {
                genreRepository.genres(query)
            } else {
                emptyList()
            }
            if (genres.isNotEmpty()) {
                results.add(context.resources.getString(R.string.genres))
                results.addAll(genres)
            }

            /** Playlists **/
            val playlist: List<PlaylistWithSongs> =
                if (filter == Filter.PLAYLISTS || filter == Filter.NO_FILTER) {
                    roomRepository.playlistWithSongs().filter { playlist ->
                        playlist.playlistEntity.playlistName.lowercase().contains(searchString.lowercase())
                    }
                } else {
                    emptyList()
                }

            if (playlist.isNotEmpty()) {
                results.add(context.getString(R.string.playlists))
                results.addAll(playlist)
            }
        }
        return results
    }
}
