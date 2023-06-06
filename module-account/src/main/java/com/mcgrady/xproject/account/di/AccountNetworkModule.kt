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
package com.mcgrady.xproject.account.di

import com.mcgrady.xproject.account.network.adapter.AccountEnvelopingConverter
import com.mcgrady.xproject.account.network.AccountService
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Created by mcgrady on 2021/8/8.
 */
@Module
@InstallIn(SingletonComponent::class)
object AccountNetworkModule {

//    @Provides
//    @Singleton
//    fun provideGson(): Gson {
//        return GsonBuilder()
//            .serializeNulls() // 序列化null
//            .enableComplexMapKeySerialization()
//            .create()
//    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okhttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .client(okhttpClient)
            .baseUrl("https://mock.apifox.cn/m1/605553-0-default/")
            .addConverterFactory(AccountEnvelopingConverter())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAccountService(retrofit: Retrofit): AccountService {
        return retrofit.create(AccountService::class.java)
    }

}
