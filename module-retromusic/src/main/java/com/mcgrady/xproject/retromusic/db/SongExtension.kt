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
package com.mcgrady.xproject.retromusic.db

import com.mcgrady.xproject.retromusic.model.Song

fun List<HistoryEntity>.fromHistoryToSongs(): List<Song> {
    return map {
        it.toSong()
    }
}

fun List<SongEntity>.toSongs(): List<Song> {
    return map {
        it.toSong()
    }
}

fun Song.toHistoryEntity(timePlayed: Long): HistoryEntity {
    return HistoryEntity(
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
        albumArtist = albumArtist,
        timePlayed = timePlayed
    )
}

fun Song.toSongEntity(playListId: Long): SongEntity {
    return SongEntity(
        playlistCreatorId = playListId,
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
    )
}

fun SongEntity.toSong(): Song {
    return Song(
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
    )
}

fun PlayCountEntity.toSong(): Song {
    return Song(
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
    )
}

fun HistoryEntity.toSong(): Song {
    return Song(
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
    )
}

fun Song.toPlayCount(): PlayCountEntity {
    return PlayCountEntity(
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
        albumArtist = albumArtist,
        timePlayed = System.currentTimeMillis(),
        playCount = 1
    )
}

fun List<Song>.toSongsEntity(playlistEntity: PlaylistEntity): List<SongEntity> {
    return map {
        it.toSongEntity(playlistEntity.playListId)
    }
}
