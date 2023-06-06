package com.mcgrady.xproject.pokemon

import com.mcgrady.xproject.pokemon.data.model.PokemonResponse
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.request

internal class PokemonApiClient constructor(private val service: PokemonApiService) {

    fun fetchPokemonList(onResult: (response: ApiResponse<PokemonResponse>) -> Unit) {
        service.fetchPokemonList(20, 1).request(onResult)
    }
}