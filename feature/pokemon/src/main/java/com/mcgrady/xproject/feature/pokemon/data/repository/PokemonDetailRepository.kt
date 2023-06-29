package com.mcgrady.xproject.feature.pokemon.data.repository

import androidx.annotation.WorkerThread
import com.mcgrady.xproject.core.repo.Repository
import com.mcgrady.xproject.feature.pokemon.data.model.PokemonInfo
import kotlinx.coroutines.flow.Flow

interface PokemonDetailRepository : Repository {

    @WorkerThread
    fun fetchPokemonInfo(
        name: String,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ): Flow<PokemonInfo>
}