package com.mcgrady.xproject.feature.pokemon.ui.viewbinding.main

import com.mcgrady.xproject.core.base.BaseViewModel
import com.mcgrady.xproject.feature.pokemon.data.repository.PokemonListRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonViewBindingViewModel @Inject constructor(
    private val repository: PokemonListRepositoryImpl
) : BaseViewModel() {


}