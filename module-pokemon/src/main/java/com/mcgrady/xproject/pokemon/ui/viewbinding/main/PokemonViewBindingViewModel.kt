package com.mcgrady.xproject.pokemon.ui.viewbinding.main

import com.mcgrady.xproject.common.core.base.BaseViewModel
import com.mcgrady.xproject.pokemon.repository.PokemonListRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonViewBindingViewModel @Inject constructor(
    private val repository: PokemonListRepositoryImpl
) : BaseViewModel() {


}