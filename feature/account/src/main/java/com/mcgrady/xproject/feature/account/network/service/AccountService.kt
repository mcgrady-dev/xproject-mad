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
package com.mcgrady.xproject.feature.account.network.service

import com.mcgrady.xproject.core.network.ApiResponse
import com.mcgrady.xproject.feature.account.data.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AccountService {

//    @GET("user")
//    fun user(): Call<BaseResponse<User>>

    @GET("user/{id}")
    fun fetchUserById(@Path("id") userId: Int): Call<User>

    @GET("user")
    fun fetchUsers(): Call<List<User>>

    @GET("user/{id}")
    suspend fun fetchUserByIdSuspend(@Path("id") userId: Int): ApiResponse<User>

    @GET("user")
    suspend fun fetchUsersSuspend(): ApiResponse<List<User>>
}
