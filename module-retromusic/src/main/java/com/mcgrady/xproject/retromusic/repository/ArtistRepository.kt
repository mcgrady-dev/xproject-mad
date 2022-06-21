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

import android.provider.MediaStore.Audio.AudioColumns
import com.mcgrady.xproject.retromusic.ALBUM_ARTIST
import com.mcgrady.xproject.retromusic.helper.SortOrder
import com.mcgrady.xproject.retromusic.model.Album
import com.mcgrady.xproject.retromusic.model.Artist
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import java.text.Collator

interface ArtistRepository {
    fun artists(): List<Artist>

    fun albumArtists(): List<Artist>

    fun albumArtists(query: String): List<Artist>

    fun artists(query: String): List<Artist>

    fun artist(artistId: Long): Artist

    fun albumArtist(artistName: String): Artist
}

class RealArtistRepository(
    private val songRepository: RealSongRepository,
    private val albumRepository: RealAlbumRepository
) : ArtistRepository {

    private fun getSongLoaderSortOrder(): String {
        return PreferenceUtil.artistSortOrder + ", " +
            PreferenceUtil.artistAlbumSortOrder + ", " +
            PreferenceUtil.artistSongSortOrder
    }

    override fun artist(artistId: Long): Artist {
        if (artistId == Artist.VARIOUS_ARTISTS_ID) {
            // Get Various Artists
            val songs = songRepository.songs(
                songRepository.makeSongCursor(
                    null,
                    null,
                    getSongLoaderSortOrder()
                )
            )
            val albums = albumRepository.splitIntoAlbums(songs)
                .filter { it.albumArtist == Artist.VARIOUS_ARTISTS_DISPLAY_NAME }
            return Artist(Artist.VARIOUS_ARTISTS_ID, albums)
        }

        val songs = songRepository.songs(
            songRepository.makeSongCursor(
                AudioColumns.ARTIST_ID + "=?",
                arrayOf(artistId.toString()),
                getSongLoaderSortOrder()
            )
        )
        return Artist(artistId, albumRepository.splitIntoAlbums(songs))
    }

    override fun albumArtist(artistName: String): Artist {
        if (artistName == Artist.VARIOUS_ARTISTS_DISPLAY_NAME) {
            // Get Various Artists
            val songs = songRepository.songs(
                songRepository.makeSongCursor(
                    null,
                    null,
                    getSongLoaderSortOrder()
                )
            )
            val albums = albumRepository.splitIntoAlbums(songs)
                .filter { it.albumArtist == Artist.VARIOUS_ARTISTS_DISPLAY_NAME }
            return Artist(Artist.VARIOUS_ARTISTS_ID, albums, true)
        }

        val songs = songRepository.songs(
            songRepository.makeSongCursor(
                "album_artist" + "=?",
                arrayOf(artistName),
                getSongLoaderSortOrder()
            )
        )
        return Artist(artistName, albumRepository.splitIntoAlbums(songs), true)
    }

    override fun artists(): List<Artist> {
        val songs = songRepository.songs(
            songRepository.makeSongCursor(
                null, null,
                getSongLoaderSortOrder()
            )
        )
        val artists = splitIntoArtists(albumRepository.splitIntoAlbums(songs))
        return sortArtists(artists)
    }

    override fun albumArtists(): List<Artist> {
        val songs = songRepository.songs(
            songRepository.makeSongCursor(
                null,
                null,
                "lower($ALBUM_ARTIST)" +
                    if (PreferenceUtil.artistSortOrder == SortOrder.ArtistSortOrder.ARTIST_A_Z) "" else " DESC"
            )
        )
        val artists = splitIntoAlbumArtists(albumRepository.splitIntoAlbums(songs))
        return sortArtists(artists)
    }

    override fun albumArtists(query: String): List<Artist> {
        val songs = songRepository.songs(
            songRepository.makeSongCursor(
                "album_artist" + " LIKE ?",
                arrayOf("%$query%"),
                getSongLoaderSortOrder()
            )
        )
        val artists = splitIntoAlbumArtists(albumRepository.splitIntoAlbums(songs))
        return sortArtists(artists)
    }

    override fun artists(query: String): List<Artist> {
        val songs = songRepository.songs(
            songRepository.makeSongCursor(
                AudioColumns.ARTIST + " LIKE ?",
                arrayOf("%$query%"),
                getSongLoaderSortOrder()
            )
        )
        val artists = splitIntoArtists(albumRepository.splitIntoAlbums(songs))
        return sortArtists(artists)
    }

    private fun splitIntoAlbumArtists(albums: List<Album>): List<Artist> {
        return albums.groupBy { it.albumArtist }
            .filter {
                !it.key.isNullOrEmpty()
            }
            .map {
                val currentAlbums = it.value
                if (currentAlbums.isNotEmpty()) {
                    if (currentAlbums[0].albumArtist == Artist.VARIOUS_ARTISTS_DISPLAY_NAME) {
                        Artist(Artist.VARIOUS_ARTISTS_ID, currentAlbums, true)
                    } else {
                        Artist(currentAlbums[0].artistId, currentAlbums, true)
                    }
                } else {
                    Artist.empty
                }
            }
    }

    fun splitIntoArtists(albums: List<Album>): List<Artist> {
        return albums.groupBy { it.artistId }
            .map { Artist(it.key, it.value) }
    }

    private fun sortArtists(artists: List<Artist>): List<Artist> {
        val collator = Collator.getInstance()
        return when (PreferenceUtil.artistSortOrder) {
            SortOrder.ArtistSortOrder.ARTIST_A_Z -> {
                artists.sortedWith { a1, a2 -> collator.compare(a1.name, a2.name) }
            }
            SortOrder.ArtistSortOrder.ARTIST_Z_A -> {
                artists.sortedWith { a1, a2 -> collator.compare(a2.name, a1.name) }
            }
            else -> artists
        }
    }
}
