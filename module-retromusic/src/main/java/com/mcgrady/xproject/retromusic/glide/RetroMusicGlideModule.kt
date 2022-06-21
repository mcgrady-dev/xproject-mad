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
package com.mcgrady.xproject.retromusic.glide

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.mcgrady.xproject.retromusic.glide.artistimage.ArtistImage
import com.mcgrady.xproject.retromusic.glide.artistimage.Factory
import com.mcgrady.xproject.retromusic.glide.audiocover.AudioFileCover
import com.mcgrady.xproject.retromusic.glide.audiocover.AudioFileCoverLoader
import com.mcgrady.xproject.retromusic.glide.palette.BitmapPaletteTranscoder
import com.mcgrady.xproject.retromusic.glide.palette.BitmapPaletteWrapper
import com.mcgrady.xproject.retromusic.glide.playlistPreview.PlaylistPreview
import com.mcgrady.xproject.retromusic.glide.playlistPreview.PlaylistPreviewLoader
import java.io.InputStream

@GlideModule
class RetroMusicGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.prepend(PlaylistPreview::class.java, Bitmap::class.java, PlaylistPreviewLoader.Factory(context))
        registry.prepend(
            AudioFileCover::class.java,
            InputStream::class.java,
            AudioFileCoverLoader.Factory()
        )
        registry.prepend(ArtistImage::class.java, InputStream::class.java, Factory(context))
        registry.register(
            Bitmap::class.java,
            BitmapPaletteWrapper::class.java, BitmapPaletteTranscoder()
        )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}
