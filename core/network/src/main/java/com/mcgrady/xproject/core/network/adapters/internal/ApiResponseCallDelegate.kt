package com.mcgrady.xproject.core.network.adapters.internal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.mcgrady.xproject.core.network.ApiResponse
import retrofit2.awaitResponse

/**
 * @author skydoves (Jaewoong Eum)
 *
 * ApiResponseCallDelegate is a delegate [Call] proxy for handling and transforming normal generic type [T]
 * as [ApiResponse] that wrapping [T] data from the network responses.
 */
internal class ApiResponseCallDelegate<T>(
    proxy: Call<T>,
    private val coroutineScope: CoroutineScope,
) : CallDelegate<T, ApiResponse<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<ApiResponse<T>>) {
        coroutineScope.launch {
            try {
                val response = proxy.awaitResponse()
                val apiResponse = ApiResponse.of { response }
                callback.onResponse(this@ApiResponseCallDelegate, Response.success(apiResponse))
            } catch (e: Exception) {
                callback.onResponse(this@ApiResponseCallDelegate, Response.success(ApiResponse.error(e)))
            }
        }
    }

    override fun executeImpl(): Response<ApiResponse<T>> =
        runBlocking(coroutineScope.coroutineContext) {
            val response = proxy.execute()
            val apiResponse = ApiResponse.of { response }
            Response.success(apiResponse)
        }

    override fun cloneImpl() = ApiResponseCallDelegate(proxy.clone(), coroutineScope)
}