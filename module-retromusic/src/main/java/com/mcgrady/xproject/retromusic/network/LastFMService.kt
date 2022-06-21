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

import com.mcgrady.xproject.retromusic.network.model.LastFmAlbum
import com.mcgrady.xproject.retromusic.network.model.LastFmArtist
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by hemanths on 2019-11-26.
 */

interface LastFMService {
    companion object {
        private const val API_KEY = "c679c8d3efa84613dc7dcb2e8d42da4c"
        const val BASE_QUERY_PARAMETERS = "?format=json&autocorrect=1&api_key=$API_KEY"
    }

    @GET("$BASE_QUERY_PARAMETERS&method=artist.getinfo")
    suspend fun artistInfo(
        @Query("artist") artistName: String,
        @Query("lang") language: String?,
        @Header("Cache-Control") cacheControl: String?
    ): LastFmArtist

    @GET("$BASE_QUERY_PARAMETERS&method=album.getinfo")
    suspend fun albumInfo(
        @Query("artist") artistName: String,
        @Query("album") albumName: String
    ): LastFmAlbum
}
