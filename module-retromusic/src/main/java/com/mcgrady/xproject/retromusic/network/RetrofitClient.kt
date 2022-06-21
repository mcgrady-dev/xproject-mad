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
package com.mcgrady.xproject.retromusic.network

import android.content.Context
import com.google.gson.GsonBuilder
import com.mcgrady.xproject.retromusic.App
import com.mcgrady.xproject.retromusic.BuildConfig
import com.mcgrady.xproject.retromusic.network.conversion.LyricsConverterFactory
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

fun provideDefaultCache(): Cache? {
    val cacheDir = File(App.getContext().cacheDir.absolutePath, "/okhttp-lastfm/")
    if (cacheDir.mkdirs() || cacheDir.isDirectory) {
        return Cache(cacheDir, 1024 * 1024 * 10)
    }
    return null
}

fun logInterceptor(): Interceptor {
    val loggingInterceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    } else {
        // disable retrofit log on release
        loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
    }
    return loggingInterceptor
}

fun headerInterceptor(context: Context): Interceptor {
    return Interceptor {
        val original = it.request()
        val request = original.newBuilder()
            .header("User-Agent", context.packageName)
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .method(original.method, original.body)
            .build()
        it.proceed(request)
    }
}

fun provideOkHttp(context: Context, cache: Cache): OkHttpClient {
    return OkHttpClient.Builder()
        .addNetworkInterceptor(logInterceptor())
        .addInterceptor(headerInterceptor(context))
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .cache(cache)
        .build()
}

fun provideLastFmRetrofit(client: OkHttpClient): Retrofit {
    val gson = GsonBuilder()
        .setLenient()
        .create()
    return Retrofit.Builder()
        .baseUrl("https://ws.audioscrobbler.com/2.0/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .callFactory { request -> client.newCall(request) }
        .build()
}

fun provideLastFmRest(retrofit: Retrofit): LastFMService {
    return retrofit.create(LastFMService::class.java)
}

fun provideDeezerRest(retrofit: Retrofit): DeezerService {
    val newBuilder = retrofit.newBuilder()
        .baseUrl("https://api.deezer.com/")
        .build()
    return newBuilder.create(DeezerService::class.java)
}

fun provideLyrics(retrofit: Retrofit): LyricsRestService {
    val newBuilder = retrofit.newBuilder()
        .baseUrl("https://makeitpersonal.co")
        .addConverterFactory(LyricsConverterFactory())
        .build()
    return newBuilder.create(LyricsRestService::class.java)
}
