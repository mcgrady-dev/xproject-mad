package com.mcgrady.xproject.feature.account.network.service

import com.mcgrady.xproject.feature.account.data.model.User
import com.mcgrady.xproject.core.network.ApiResponse
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