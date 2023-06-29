package com.mcgrady.xproject.core.network.adapters.internal

import com.mcgrady.xproject.core.network.ApiResponse
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.awaitResponse
import java.lang.reflect.Type

/**
 * @author skydoves (Jaewoong Eum)
 *
 * ApiResponseCallAdapter is an call adapter for creating [ApiResponse] by executing Retrofit's service methods.
 *
 * Request API network call asynchronously and returns [Deferred] of [ApiResponse].
 */
internal class ApiResponseDeferredCallAdapter<T> constructor(
    private val resultType: Type,
    private val coroutineScope: CoroutineScope,
) : CallAdapter<T, Deferred<ApiResponse<T>>> {

    override fun responseType(): Type {
        return resultType
    }

    @Suppress("DeferredIsResult")
    override fun adapt(call: Call<T>): Deferred<ApiResponse<T>> {
        val deferred = CompletableDeferred<ApiResponse<T>>().apply {
            invokeOnCompletion {
                if (isCancelled && !call.isCanceled) {
                    call.cancel()
                }
            }
        }

        coroutineScope.launch {
            try {
                val response = call.awaitResponse()
                val apiResponse = ApiResponse.of { response }
                deferred.complete(apiResponse)
            } catch (e: Exception) {
                val apiResponse = ApiResponse.error<T>(e)
                deferred.complete(apiResponse)
            }
        }

        return deferred
    }
}