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
package com.mcgrady.xproject.feature.pokemon.data.di

import com.mcgrady.xproject.feature.pokemon.data.repository.PokemonDetailRepository
import com.mcgrady.xproject.feature.pokemon.data.repository.PokemonDetailRepositoryImpl
import com.mcgrady.xproject.feature.pokemon.data.repository.PokemonListRepository
import com.mcgrady.xproject.feature.pokemon.data.repository.PokemonListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by mcgrady on 2022/1/7.
 */
@Module
@InstallIn(SingletonComponent::class)
interface PokemonDataModule {

    @Binds
    fun bindsPokemonListRepository(pokemonListRepositoryImpl: PokemonListRepositoryImpl): PokemonListRepository

    @Binds
    fun bindsPokemonDetailRepository(pokemonDetailRepositoryImpl: PokemonDetailRepositoryImpl): PokemonDetailRepository
}