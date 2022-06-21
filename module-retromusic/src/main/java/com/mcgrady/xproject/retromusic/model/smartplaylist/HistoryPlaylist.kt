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
package com.mcgrady.xproject.retromusic.model.smartplaylist

import com.mcgrady.xproject.retromusic.App
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.model.Song
import kotlinx.parcelize.Parcelize
import org.koin.core.component.KoinComponent

@Parcelize
class HistoryPlaylist :
    AbsSmartPlaylist(
        name = App.getContext().getString(R.string.history),
        iconRes = R.drawable.ic_history
    ),
    KoinComponent {

    override fun songs(): List<Song> {
        return topPlayedRepository.recentlyPlayedTracks()
    }
}
