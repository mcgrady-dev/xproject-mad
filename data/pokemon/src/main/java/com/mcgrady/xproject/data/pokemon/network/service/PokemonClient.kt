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
package com.mcgrady.xproject.data.pokemon.network.service

import com.mcgrady.xproject.core.network.ApiResponse
import com.mcgrady.xproject.data.pokemon.model.PokemonInfo
import com.mcgrady.xproject.data.pokemon.model.PokemonResponse
import javax.inject.Inject

/**
 * Created by mcgrady on 2021/8/12.
 */
class PokemonClient @Inject constructor(private val pokemonService: PokemonService) {
    suspend fun fetchPokemonList(
        page: Int,
    ): ApiResponse<PokemonResponse> = pokemonService.fetchPokemonList(
        limit = PAGING_SIZE,
        offset = page * PAGING_SIZE,
    )

    suspend fun fetchPokemonInfo(name: String): ApiResponse<PokemonInfo> =
        pokemonService.fetchPokemonInfo(name = name)

    companion object {
        private const val PAGING_SIZE = 20
    }
}
