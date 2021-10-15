package com.mcgrady.xproject

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by mcgrady on 2021/8/12.
 */
interface ApiService {

    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonResponse
}