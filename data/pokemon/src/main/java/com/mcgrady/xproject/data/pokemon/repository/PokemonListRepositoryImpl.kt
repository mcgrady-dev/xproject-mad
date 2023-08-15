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
package com.mcgrady.xproject.data.pokemon.repository

import androidx.annotation.WorkerThread
import com.mcgrady.xproject.core.network.message
import com.mcgrady.xproject.core.network.onFailure
import com.mcgrady.xproject.core.network.suspendOnSuccess
import com.mcgrady.xproject.data.pokemon.database.dao.PokemonDao
import com.mcgrady.xproject.data.pokemon.database.entity.mapper.asDomain
import com.mcgrady.xproject.data.pokemon.database.entity.mapper.asEntity
import com.mcgrady.xproject.data.pokemon.network.service.PokemonClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * Created by mcgrady on 2021/10/15.
 */
class PokemonListRepositoryImpl @Inject constructor(
    private val pokemonClient: PokemonClient,
    private val pokemonDao: PokemonDao,
) : PokemonListRepository {

    @WorkerThread
    override fun fetchPokemonList(
        page: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
    ) = flow {
        var pokemons = pokemonDao.getPokemonList(page).asDomain()
        if (pokemons.isEmpty()) {
            val response = pokemonClient.fetchPokemonList(page = page)
            response.suspendOnSuccess {
                pokemons = data.results
                pokemons.forEach { pokemon -> pokemon.page = page }
                pokemonDao.insertPokemonList(pokemons.asEntity())
                emit(pokemons)
            }.onFailure {
                onError(message())
            }
        } else {
            emit(pokemonDao.getAllPokemonList(page).asDomain())
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)
}
