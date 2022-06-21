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
package com.mcgrady.xproject.retromusic.glide.playlistPreview

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey

class PlaylistPreviewLoader(val context: Context) : ModelLoader<PlaylistPreview, Bitmap> {
    override fun buildLoadData(
        model: PlaylistPreview,
        width: Int,
        height: Int,
        options: Options
    ): LoadData<Bitmap> {
        return LoadData(
            ObjectKey(model),
            PlaylistPreviewFetcher(context, model)
        )
    }

    override fun handles(model: PlaylistPreview): Boolean {
        return true
    }

    class Factory(val context: Context) : ModelLoaderFactory<PlaylistPreview, Bitmap> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<PlaylistPreview, Bitmap> {
            return PlaylistPreviewLoader(context)
        }

        override fun teardown() {}
    }
}
