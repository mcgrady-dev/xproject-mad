package com.mcgrady.xproject.pokemon.ui.databinding.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.mcgrady.xproject.common.core.base.BaseViewModel
import com.mcgrady.xproject.pokemon.data.model.PokemonInfo
import com.mcgrady.xproject.pokemon.repository.PokemonDetailRepository
import com.mcgrady.xproject.pokemon.repository.PokemonListRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PokemonDetailDBViewModel @AssistedInject constructor(
    private val repository: PokemonDetailRepository,
    @Assisted private val pokemonName: String
) : BaseViewModel() {

    private val pokemonInfoFlow: Flow<PokemonInfo?> = repository.fetchPokemonInfo(
        name = pokemonName,
        onStart = { _isLoading.postValue(true) },
        onComplete = { _isLoading.postValue(false) },
        onError = { _toast.postValue(it) }
    )

    val pokemonInfo: LiveData<PokemonInfo?> = pokemonInfoFlow.asLiveDataOnViewModelScope()

    @AssistedFactory
    interface PokemonDetailDBViewModelFactory {
        fun create(pokemonName: String): PokemonDetailDBViewModel
    }

    fun getPokemonInfo() {
        viewModelScope.launch {
            pokemonInfoFlow.catch {

            }.collect { pokemonInfo ->

            }
        }
    }

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