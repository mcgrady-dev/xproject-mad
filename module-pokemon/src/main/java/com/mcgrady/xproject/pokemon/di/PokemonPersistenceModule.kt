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
package com.mcgrady.xproject.pokemon.di

import android.app.Application
import androidx.room.Room
import com.mcgrady.xproject.pokemon.data.database.dao.PokemonDao
import com.mcgrady.xproject.pokemon.data.database.dao.PokemonInfoDao
import com.mcgrady.xproject.pokemon.data.database.PokemonDatabase
import com.mcgrady.xproject.pokemon.data.database.TypeResponseConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

/**
 * Created by mcgrady on 2022/1/7.
 */
@Module
@InstallIn(SingletonComponent::class)
object PokemonPersistenceModule {

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }

    @Provides
    @Singleton
    fun providePokemonDataBase(application: Application, typeResponseConverter: TypeResponseConverter): PokemonDatabase {
        return Room.databaseBuilder(application, PokemonDatabase::class.java, "pokemon.db")
            .fallbackToDestructiveMigration()
            .addTypeConverter(typeResponseConverter)
            .build()
    }

    @Provides
    @Singleton
    fun providePokemonDao(database: PokemonDatabase): PokemonDao {
        return database.pokemonDao()
    }

    @Provides
    @Singleton
    fun providePokemonInfoDao(database: PokemonDatabase): PokemonInfoDao {
        return database.pokemonInfoDao()
    }

    @Provides
    @Singleton
    fun provideTypeResponseConverter(json: Json): TypeResponseConverter {
        return TypeResponseConverter(json)
    }
}
