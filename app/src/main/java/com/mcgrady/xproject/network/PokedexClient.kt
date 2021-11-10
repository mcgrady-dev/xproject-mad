package com.mcgrady.xproject.network

import com.mcgrady.xproject.PokemonResponse
import javax.inject.Inject

/**
 * Created by mcgrady on 2021/8/12.
 */
class PokedexClient @Inject constructor(private val service: PokedexService) {

    suspend fun fetchPokemonList(
        page: Int
    ): PokemonResponse = service.fetchPokemonList(
        limit = PAGING_SIZE,
        offset = page * PAGING_SIZE
    )

    companion object {
        private const val PAGING_SIZE = 20
    }
}