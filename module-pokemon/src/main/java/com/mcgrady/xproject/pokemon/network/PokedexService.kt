package com.mcgrady.xproject.pokemon.network

import com.mcgrady.xproject.pokemon.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by mcgrady on 2021/8/12.
 */
interface PokedexService {

    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonResponse
}