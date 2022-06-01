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
package com.mcgrady.xproject.pokemon.ui.main

import androidx.annotation.MainThread
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import com.mcgrady.xproject.common.core.base.BaseViewModel
import com.mcgrady.xproject.pokemon.model.Pokemon
import com.mcgrady.xproject.pokemon.repo.PokedexRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * Created by mcgrady on 2022/1/7.
 */
@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val repository: PokedexRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val pokemonFetchingIndex: MutableStateFlow<Int> = MutableStateFlow(0)
    val pokemonListLiveData: LiveData<List<Pokemon>>

    private val _toastLiveData: MutableLiveData<String> = MutableLiveData()
    val toastLiveData: LiveData<String> get() = _toastLiveData

    val isLoading: ObservableBoolean = ObservableBoolean(false)

    init {
        pokemonListLiveData = pokemonFetchingIndex.asLiveData().switchMap { page ->
            repository.fetchPokemonList(
                page = page,
                onStart = { isLoading.set(true) },
                onComplete = { isLoading.set(false) },
                onError = { _toastLiveData.postValue(it) }
            ).asLiveDataOnViewModelScope()
        }
    }

    //    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchPokemonInfoInLiveData(page: Int) = liveData<List<Pokemon>> {
        repository.fetchPokemonList(
            page = page,
            onStart = {},
            onComplete = {},
            onError = {}
        )
    }

    //    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun fetchPokemonInfoAsLiveData(page: Int) = repository.fetchPokemonList(
        page = page,
        onStart = {},
        onComplete = {},
        onError = {}
    ).asLiveData()

    @MainThread
    fun fetchPokemonList() {
        if (!isLoading.get()) {
            pokemonFetchingIndex.value++
        }
    }
}