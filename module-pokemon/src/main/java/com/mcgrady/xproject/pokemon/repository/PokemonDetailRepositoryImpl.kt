package com.mcgrady.xproject.pokemon.repository

import androidx.annotation.WorkerThread
import com.mcgrady.xproject.common.core.network.message
import com.mcgrady.xproject.common.core.network.onError
import com.mcgrady.xproject.common.core.network.onException
import com.mcgrady.xproject.common.core.network.suspendOnSuccess
import com.mcgrady.xproject.pokemon.data.database.dao.PokemonInfoDao
import com.mcgrady.xproject.pokemon.data.database.entity.mapper.asDomain
import com.mcgrady.xproject.pokemon.data.database.entity.mapper.asEntity
import com.mcgrady.xproject.pokemon.service.PokemonClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class PokemonDetailRepositoryImpl @Inject constructor(
    private val pokemonClient: PokemonClient,
    private val pokemonInfoDao: PokemonInfoDao
) : PokemonDetailRepository {

    @WorkerThread
    override fun fetchPokemonInfo(
        name: String,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
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