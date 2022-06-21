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
package com.mcgrady.xproject.retromusic

import androidx.annotation.IntDef

@IntDef(
    RECENT_ALBUMS,
    TOP_ALBUMS,
    RECENT_ARTISTS,
    TOP_ARTISTS,
    SUGGESTIONS,
    FAVOURITES,
    GENRES,
    PLAYLISTS
)
@Retention(AnnotationRetention.SOURCE)
annotation class HomeSection

const val RECENT_ALBUMS = 3
const val TOP_ALBUMS = 1
const val RECENT_ARTISTS = 2
const val TOP_ARTISTS = 0
const val SUGGESTIONS = 5
const val FAVOURITES = 4
const val GENRES = 6
const val PLAYLISTS = 7
const val HISTORY_PLAYLIST = 8
const val LAST_ADDED_PLAYLIST = 9
const val TOP_PLAYED_PLAYLIST = 10
