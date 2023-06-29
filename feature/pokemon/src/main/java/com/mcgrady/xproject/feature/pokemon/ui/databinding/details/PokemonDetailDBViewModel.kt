package com.mcgrady.xproject.feature.pokemon.ui.databinding.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mcgrady.xproject.core.base.BaseViewModel
import com.mcgrady.xproject.feature.pokemon.data.model.PokemonInfo
import com.mcgrady.xproject.feature.pokemon.data.repository.PokemonDetailRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class PokemonDetailDBViewModel @AssistedInject constructor(
    repository: PokemonDetailRepository,
    @Assisted private val pokemonName: String
) : BaseViewModel() {

    private val pokemonInfoFlow: Flow<PokemonInfo> = repository.fetchPokemonInfo(
        name = pokemonName,
        onStart = { _isLoading.postValue(true) },
        onComplete = { _isLoading.postValue(false) },
        onError = { _toast.postValue(it) }
    )

    val pokemonInfo: LiveData<PokemonInfo> = pokemonInfoFlow.asLiveDataOnViewModelScope()

    @AssistedFactory
    interface PokemonDetailDBViewModelFactory {
        fun create(pokemonName: String): PokemonDetailDBViewModel
    }

//    fun getPokemonInfo() {
//        viewModelScope.launch {
//            pokemonInfoFlow.catch {
//
//            }.collect { pokemonInfo ->
//
//            }
//        }
//    }

    companion object {

        fun provideFactory(
            factory: PokemonDetailDBViewModelFactory,
            pokemonName: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory.create(pokemonName) as T
            }
        }
    }

}