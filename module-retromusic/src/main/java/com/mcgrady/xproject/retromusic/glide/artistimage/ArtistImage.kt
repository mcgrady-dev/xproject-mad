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
package com.mcgrady.xproject.retromusic.glide.artistimage

import com.mcgrady.xproject.retromusic.model.Artist

class ArtistImage(val artist: Artist) {
    override fun equals(other: Any?): Boolean {
        if (other is ArtistImage) {
            return other.artist == artist
        }
        return false
    }

    override fun hashCode(): Int {
        return artist.hashCode()
    }
}
