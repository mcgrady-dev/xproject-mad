package com.mcgrady.xproject.pokemon

import com.mcgrady.xproject.common.core.network.ApiResponse
import com.mcgrady.xproject.common.core.network.request
import com.mcgrady.xproject.pokemon.data.model.PokemonResponse

internal class PokemonApiClient constructor(private val service: PokemonApiService) {

    fun fetchPokemonList(onResult: (response: ApiResponse<PokemonResponse>) -> Unit) {
        service.fetchPokemonList(20, 1).request(onResult)
    }
}