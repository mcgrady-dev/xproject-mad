package com.mcgrady.xproject.feature.pokemon

import com.mcgrady.xproject.core.network.ApiResponse
import com.mcgrady.xproject.core.network.request
import com.mcgrady.xproject.feature.pokemon.data.model.PokemonResponse

internal class PokemonApiClient constructor(private val service: PokemonApiService) {

    fun fetchPokemonList(onResult: (response: ApiResponse<PokemonResponse>) -> Unit) {
        service.fetchPokemonList(20, 1).request(onResult)
    }
}