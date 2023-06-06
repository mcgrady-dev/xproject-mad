package com.mcgrady.xproject.common.core.network

import com.mcgrady.xproject.common.core.network.exceptions.NoContentException
import com.mcgrady.xproject.common.core.network.operators.ApiResponseOperator
import com.mcgrady.xproject.common.core.network.operators.ApiResponseSuspendOperator
import kotlinx.coroutines.launch
import okhttp3.Headers
import okhttp3.ResponseBody
import retrofit2.Response

sealed interface ApiResponse<out T> {

    data class Success<T>(val response: Response<T>) : ApiResponse<T> {
        val statusCode: StatusCode = getStatusCodeFromResponse(response)
        val headers: Headers = response.headers()
        val raw: okhttp3.Response = response.raw()
        val data: T by lazy { response.body() ?: throw NoContentException(statusCode.code) }
        override fun toString(): String = "[ApiResponse.Success](data=$data)"
    }

    sealed interface Failure<T> : ApiResponse<T> {

        data class Error<T>(val response: Response<T>) : Failure<T> {
            val statusCode: StatusCode = getStatusCodeFromResponse(response)
            val headers: Headers = response.headers()
            val raw: okhttp3.Response = response.raw()
            val errorBody: ResponseBody? = response.errorBody()
            override fun toString(): String {
                val errorBody = errorBody?.string()
                return if (!errorBody.isNullOrEmpty()) {
                    errorBody
                } else {
                    "[ApiResponse.Failure.Error-$statusCode](errorResponse=$response)"
                }
            }
        }

        data class Exception<T>(val exception: Throwable) : Failure<T> {
            val message: String? = exception.localizedMessage
            override fun toString(): String = "[ApiResponse.Failure.Exception](message=$message)"
        }
    }

    companion object {

        fun <T> error(ex: Throwable): Failure.Exception<T> =
            Failure.Exception<T>(ex).apply { operate() }


        @JvmSynthetic
        inline fun <T> of(
            successCodeRange: IntRange = NetworkInitializer.successCodeRange,
            crossinline f: () -> Response<T>,
        ): ApiResponse<T> = try {
            val response = f()
            if (response.raw().code in successCodeRange) {
                Success(response)
            } else {
                Failure.Error(response)
            }
        } catch (ex: Exception) {
            Failure.Exception(ex)
        }.operate()

        @PublishedApi
        @Suppress("UNCHECKED_CAST")
        internal fun <T> ApiResponse<T>.operate(): ApiResponse<T> = apply {
            val globalOperators = NetworkInitializer.sandwichOperators
            globalOperators.forEach { globalOperator ->
                if (globalOperator is ApiResponseOperator<*>) {
                    operator(globalOperator as ApiResponseOperator<T>)
                } else if (globalOperator is ApiResponseSuspendOperator<*>) {
                    val scope = NetworkInitializer.sandwichScope
                    scope.launch {
                        suspendOperator(globalOperator as ApiResponseSuspendOperator<T>)
                    }
                }
            }
        }

        fun <T> getStatusCodeFromResponse(response: Response<T>): StatusCode {
            return StatusCode.values().find { it.code == response.code() }
                ?: StatusCode.Unknown
        }
    }
}