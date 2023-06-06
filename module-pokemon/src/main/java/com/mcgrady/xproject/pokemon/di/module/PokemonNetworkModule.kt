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
package com.mcgrady.xproject.pokemon.di.module

import com.mcgrady.xproject.pokemon.network.PokemonClient
import com.mcgrady.xproject.pokemon.network.PokemonService
import com.mcgrady.xproject.pokemon.network.asConverterFactory
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by mcgrady on 2021/8/8.
 */
@Module
@InstallIn(SingletonComponent::class)
object PokemonNetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(okhttpClient: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .client(okhttpClient)
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePokedexService(retrofit: Retrofit): PokemonService {
        return retrofit.create(PokemonService::class.java)
    }

    @Provides
    @Singleton
    fun providePokedexClient(pokedexService: PokemonService): PokemonClient {
        return PokemonClient(pokedexService)
    }
}
