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

import kotlinx.parcelize.Parcelize

/**
 * Created by hemanths on 3/4/19
 */
@Parcelize
class PlaylistSong(
    override val id: Long,
    override val title: String,
    override val trackNumber: Int,
    override val year: Int,
    override val duration: Long,
    override val data: String,
    override val dateModified: Long,
    override val albumId: Long,
    override val albumName: String,
    override val artistId: Long,
    override val artistName: String,
    val playlistId: Long,
    val idInPlayList: Long,
    override val composer: String?,
    override val albumArtist: String?
) : Song(
    id = id,
    title = title,
    trackNumber = trackNumber,
    year = year,
    duration = duration,
    data = data,
    dateModified = dateModified,
    albumId = albumId,
    albumName = albumName,
    artistId = artistId,
    artistName = artistName,
    composer = composer,
    albumArtist = albumArtist
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as PlaylistSong

        if (id != other.id) return false
        if (title != other.title) return false
        if (trackNumber != other.trackNumber) return false
        if (year != other.year) return false
        if (duration != other.duration) return false
        if (data != other.data) return false
        if (dateModified != other.dateModified) return false
        if (albumId != other.albumId) return false
        if (albumName != other.albumName) return false
        if (artistId != other.artistId) return false
        if (artistName != other.artistName) return false
        if (playlistId != other.playlistId) return false
        if (idInPlayList != other.idInPlayList) return false
        if (composer != other.composer) return false
        if (albumArtist != other.albumArtist) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + trackNumber
        result = 31 * result + year
        result = 31 * result + duration.hashCode()
        result = 31 * result + data.hashCode()
        result = 31 * result + dateModified.hashCode()
        result = 31 * result + albumId.hashCode()
        result = 31 * result + albumName.hashCode()
        result = 31 * result + artistId.hashCode()
        result = 31 * result + artistName.hashCode()
        result = 31 * result + playlistId.hashCode()
        result = 31 * result + idInPlayList.hashCode()
        result = 31 * result + composer.hashCode()
        result = 31 * result + (albumArtist?.hashCode() ?: 0)
        return result
    }
}
