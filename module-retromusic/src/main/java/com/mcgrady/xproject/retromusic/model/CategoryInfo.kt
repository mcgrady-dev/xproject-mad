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

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mcgrady.xproject.retromusic.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryInfo(
    val category: Category,
    var visible: Boolean
) : Parcelable {

    enum class Category(
        val id: Int,
        @StringRes val stringRes: Int,
        @DrawableRes val icon: Int
    ) {
        Home(R.id.action_home, R.string.for_you, R.drawable.asld_face),
        Songs(R.id.action_song, R.string.songs, R.drawable.asld_music_note),
        Albums(R.id.action_album, R.string.albums, R.drawable.asld_album),
        Artists(R.id.action_artist, R.string.artists, R.drawable.asld_artist),
        Playlists(R.id.action_playlist, R.string.playlists, R.drawable.asld_playlist),
        Genres(R.id.action_genre, R.string.genres, R.drawable.asld_guitar),
        Folder(R.id.action_folder, R.string.folders, R.drawable.asld_folder),
        Search(R.id.action_search, R.string.action_search, R.drawable.ic_search);
    }
}
