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
package com.mcgrady.xproject.retromusic.fragments.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mcgrady.xproject.retromusic.db.PlaylistWithSongs
import com.mcgrady.xproject.retromusic.db.SongEntity
import com.mcgrady.xproject.retromusic.repository.RealRepository

class PlaylistDetailsViewModel(
    private val realRepository: RealRepository,
    private var playlist: PlaylistWithSongs
) : ViewModel() {
    fun getSongs(): LiveData<List<SongEntity>> =
        realRepository.playlistSongs(playlist.playlistEntity.playListId)

    fun playlistExists(): LiveData<Boolean> =
        realRepository.checkPlaylistExists(playlist.playlistEntity.playListId)
}
