package com.mcgrady.xproject.pokemon.di

import com.mcgrady.xproject.pokemon.network.PokedexClient
import com.mcgrady.xproject.pokemon.repo.PokedexRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Created by mcgrady on 2022/1/7.
 */
@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun providePokedexRepository(pokedexClient: PokedexClient): PokedexRepository {
        return PokedexRepository(pokedexClient)
    }
}