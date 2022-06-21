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

import android.content.Context
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import com.mcgrady.xproject.retromusic.network.DeezerService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.InputStream
import java.util.concurrent.TimeUnit

class ArtistImageLoader(
    val context: Context,
    private val deezerService: DeezerService,
    private val okhttp: OkHttpClient
) : ModelLoader<ArtistImage, InputStream> {

    override fun buildLoadData(
        model: ArtistImage,
        width: Int,
        height: Int,
        options: Options
    ): LoadData<InputStream> {
        return LoadData(
            ObjectKey(model.artist.name),
            ArtistImageFetcher(context, deezerService, model, okhttp)
        )
    }

    override fun handles(model: ArtistImage): Boolean {
        return true
    }
}

class Factory(
    val context: Context
) : ModelLoaderFactory<ArtistImage, InputStream> {

    private var deezerService = DeezerService.invoke(
        DeezerService.createDefaultOkHttpClient(context)
            .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(createLogInterceptor())
            .build()
    )

    private var okHttp = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        .build()

    private fun createLogInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<ArtistImage, InputStream> {
        return ArtistImageLoader(
            context,
            deezerService,
            okHttp
        )
    }

    override fun teardown() {}

    companion object {
        // we need these very low values to make sure our artist image loading calls doesn't block the image loading queue
        private const val TIMEOUT: Long = 500
    }
}
