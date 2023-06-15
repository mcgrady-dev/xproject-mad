package com.mcgrady.xproject.pokemon.repository

import androidx.annotation.WorkerThread
import com.mcgrady.xproject.common.core.repo.Repository
import com.mcgrady.xproject.pokemon.data.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonListRepository : Repository {

    @WorkerThread
    fun fetchPokemonList(
        page: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ): Flow<List<Pokemon>>
}