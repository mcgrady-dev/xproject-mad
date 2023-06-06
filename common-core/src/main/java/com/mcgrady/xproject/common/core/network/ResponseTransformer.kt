package com.mcgrady.xproject.common.core.network

import com.mcgrady.xproject.common.core.network.adapters.internal.SuspensionFunction
import com.mcgrady.xproject.common.core.network.operators.ApiResponseOperator
import com.mcgrady.xproject.common.core.network.operators.ApiResponseSuspendOperator


@JvmSynthetic
fun <T, V : ApiResponseOperator<T>> ApiResponse<T>.operator(
    apiResponseOperator: V,
): ApiResponse<T> = apply {
    when (this) {
        is ApiResponse.Success -> apiResponseOperator.onSuccess(this)
        is ApiResponse.Failure.Error -> apiResponseOperator.onError(this)
        is ApiResponse.Failure.Exception -> apiResponseOperator.onException(this)
    }
}

@JvmSynthetic
@SuspensionFunction
suspend fun <T, V : ApiResponseSuspendOperator<T>> ApiResponse<T>.suspendOperator(
    apiResponseOperator: V,
): ApiResponse<T> = apply {
    when (this) {
        is ApiResponse.Success -> apiResponseOperator.onSuccess(this)
        is ApiResponse.Failure.Error -> apiResponseOperator.onError(this)
        is ApiResponse.Failure.Exception -> apiResponseOperator.onException(this)
    }
}