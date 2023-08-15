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
import com.mcgrady.xproject.core.network.onError
import com.mcgrady.xproject.core.network.onException
import com.mcgrady.xproject.core.network.suspendOnSuccess
import com.mcgrady.xproject.data.pokemon.database.dao.PokemonInfoDao
import com.mcgrady.xproject.data.pokemon.database.entity.mapper.asDomain
import com.mcgrady.xproject.data.pokemon.database.entity.mapper.asEntity
import com.mcgrady.xproject.data.pokemon.network.service.PokemonClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class PokemonDetailRepositoryImpl @Inject constructor(
    private val pokemonClient: PokemonClient,
    private val pokemonInfoDao: PokemonInfoDao,
) : PokemonDetailRepository {

    @WorkerThread
    override fun fetchPokemonInfo(
        name: String,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
    ) = flow {
        val pokemonInfoEntity = pokemonInfoDao.getPokemonInfo(name)
        if (pokemonInfoEntity == null) {
            val response = pokemonClient.fetchPokemonInfo(name)
            response.suspendOnSuccess {
                pokemonInfoDao.insertPokemonInfo(data.asEntity())
                emit(data)
            }.onError {
                onError("[code: ${statusCode.code}]: ${message()}")
            }.onException { onError(message) }
        } else {
            emit(pokemonInfoEntity.asDomain())
        }
    }.onStart { onStart }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)
}
