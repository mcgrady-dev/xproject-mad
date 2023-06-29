package com.mcgrady.xproject.feature.pokemon.data.repository

import androidx.annotation.WorkerThread
import com.mcgrady.xproject.core.repo.Repository
import com.mcgrady.xproject.feature.pokemon.data.model.Pokemon
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