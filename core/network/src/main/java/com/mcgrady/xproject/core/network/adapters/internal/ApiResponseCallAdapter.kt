package com.mcgrady.xproject.core.network.adapters.internal

import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type
import com.mcgrady.xproject.core.network.ApiResponse

/**
 * @author skydoves (Jaewoong Eum)
 *
 * ApiResponseCallAdapter is an call adapter for creating [ApiResponse] by executing Retrofit's service methods.
 *
 * Request API network call asynchronously and returns [ApiResponse].
 */
internal class ApiResponseCallAdapter constructor(
    private val resultType: Type,
    private val coroutineScope: CoroutineScope,
) : CallAdapter<Type, Call<ApiResponse<Type>>> {

    override fun responseType(): Type {
        return resultType
    }

    override fun adapt(call: Call<Type>): Call<ApiResponse<Type>> {
        return ApiResponseCallDelegate(call, coroutineScope)
    }
}