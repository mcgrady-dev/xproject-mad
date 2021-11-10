package com.mcgrady.xproject.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mcgrady.xproject.network.PokedexClient
import com.mcgrady.xproject.network.PokedexService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit.GsonConverterFactory
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by mcgrady on 2021/8/8.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .serializeNulls() //序列化null
            .enableComplexMapKeySerialization()
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create(gson) as Converter.Factory)
//            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun providePokedexService(retrofit: Retrofit): PokedexService {
        return retrofit.create(PokedexService::class.java)
    }

    @Provides
    @Singleton
    fun providePokedexClient(pokedexService: PokedexService): PokedexClient {
        return PokedexClient(pokedexService)
    }
}