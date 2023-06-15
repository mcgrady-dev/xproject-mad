package com.mcgrady.xproject.common.core.network.adapters

import com.mcgrady.xproject.common.core.network.ApiResponse
import com.mcgrady.xproject.common.core.network.NetworkInitializer
import com.mcgrady.xproject.common.core.network.adapters.internal.ApiResponseCallAdapter
import com.mcgrady.xproject.common.core.network.adapters.internal.ApiResponseDeferredCallAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author skydoves (Jaewoong Eum)
 *
 * CoroutinesResponseCallAdapterFactory is an coroutines call adapter factory for creating [ApiResponse].
 *
 * Adding this class to [Retrofit] allows you to return on [ApiResponse] from service method.
 *
 * ```
 * @GET("DisneyPosters.json")
 * suspend fun fetchDisneyPosterList(): ApiResponse<List<Poster>>
 * ```
 */
class ApiResponseCallAdapterFactory private constructor(
    private val coroutineScope: CoroutineScope,
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        when (getRawType(returnType)) {
            Call::class.java -> {
                val callType = getParameterUpperBound(0, returnType as ParameterizedType)
                val rawType = getRawType(callType)
                if (rawType != ApiResponse::class.java) {
                    return null
                }

                val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                return ApiResponseCallAdapter(resultType, coroutineScope)
            }

            Deferred::class.java -> {
                val callType = getParameterUpperBound(0, returnType as ParameterizedType)
                val rawType = getRawType(callType)
                if (rawType != ApiResponse::class.java) {
                    return null
                }

                val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                return ApiResponseDeferredCallAdapter<Any>(resultType, coroutineScope)
            }

            else -> return null
        }
    }

    companion object {
        @JvmStatic
        fun create(coroutineScope: CoroutineScope = NetworkInitializer.coroutineScope): ApiResponseCallAdapterFactory =
            ApiResponseCallAdapterFactory(coroutineScope)
    }
}
