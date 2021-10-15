package com.mcgrady.xproject

import javax.inject.Inject

/**
 * Created by mcgrady on 2021/8/12.
 */
class ApiClient @Inject constructor(private val service: ApiService) {

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