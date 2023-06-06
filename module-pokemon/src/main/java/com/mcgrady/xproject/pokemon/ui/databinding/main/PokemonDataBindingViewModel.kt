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
package com.mcgrady.xproject.pokemon.ui.databinding.main

import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.mcgrady.xproject.common.core.base.BaseViewModel
import com.mcgrady.xproject.pokemon.data.model.Pokemon
import com.mcgrady.xproject.pokemon.repo.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * Created by mcgrady on 2022/1/7.
 */
@HiltViewModel
class PokemonDataBindingViewModel @Inject constructor(
    private val repository: PokemonRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val pokemonFetchingIndex: MutableStateFlow<Int> = MutableStateFlow(0)
    val pokemonListLiveData: LiveData<List<Pokemon>> =
        pokemonFetchingIndex.asLiveData().switchMap { page ->
            repository.fetchPokemonList(
                page = page,
                onStart = { _isLoading.postValue(true) },
                onComplete = { _isLoading.postValue(false) },
                onError = { _toast.postValue(it) }
            ).asLiveDataOnViewModelScope()
        }

    @MainThread
    fun fetchPokemonList() {
        if (isLoading.value == false) {
            pokemonFetchingIndex.value++
        }
    }
}
