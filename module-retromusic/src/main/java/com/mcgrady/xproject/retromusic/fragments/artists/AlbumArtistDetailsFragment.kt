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
package com.mcgrady.xproject.retromusic.fragments.artists

import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AlbumArtistDetailsFragment : AbsArtistDetailsFragment() {

    private val arguments by navArgs<AlbumArtistDetailsFragmentArgs>()

    override val detailsViewModel: ArtistDetailsViewModel by viewModel {
        parametersOf(null, arguments.extraArtistName)
    }
    override val artistId: Long?
        get() = null
    override val artistName: String
        get() = arguments.extraArtistName
}
