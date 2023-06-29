package com.mcgrady.xproject.core.network


import com.mcgrady.xproject.core.network.adapters.internal.SuspensionFunction
import com.mcgrady.xproject.core.network.operators.ApiResponseOperator
import com.mcgrady.xproject.core.network.operators.ApiResponseSuspendOperator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author skydoves (Jaewoong Eum)
 *
 * Requests asynchronously and executes the lambda that receives [ApiResponse] as a result.
 *
 * @param onResult An lambda that receives [ApiResponse] as a result.
 *
 * @return The original [Call].
 */
@JvmSynthetic
inline fun <T> Call<T>.request(
    crossinline onResult: (response: ApiResponse<T>) -> Unit,
): Call<T> = apply {
    enqueue(getCallbackFromOnResult(onResult))
}

/**
 * @author skydoves (Jaewoong Eum)
 *
 * Returns a response callback from an onResult lambda.
 *
 * @param onResult A lambda that would be executed when the request finished.
 *
 * @return A [Callback] will be executed.
 */
@PublishedApi
@JvmSynthetic
internal inline fun <T> getCallbackFromOnResult(
    crossinline onResult: (response: ApiResponse<T>) -> Unit,
): Callback<T> {
    return object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            onResult(ApiResponse.of { response })
        }

        override fun onFailure(call: Call<T>, throwable: Throwable) {
            onResult(ApiResponse.error(throwable))
        }
    }
}

/**
 * @author skydoves (Jaewoong Eum)
 *
 * Returns a response callback from an onResult lambda.
 *
 * @param onResult A lambda that would be executed when the request finished.
 *
 * @return A [Callback] will be executed.
 */
@PublishedApi
@JvmSynthetic
internal inline fun <T> getCallbackFromOnResultOnCoroutinesScope(
    coroutineScope: CoroutineScope,
    crossinline onResult: suspend (response: ApiResponse<T>) -> Unit,
): Callback<T> {
    return object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            coroutineScope.launch {
                onResult(ApiResponse.of { response })
            }
        }

        override fun onFailure(call: Call<T>, throwable: Throwable) {
            coroutineScope.launch {
                onResult(ApiResponse.error(throwable))
            }
        }
    }
}

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A suspension scope function that would be executed for handling successful responses if the request succeeds.
 *
 * @param onResult The receiver function that receiving [ApiResponse.Success] if the request succeeds.
 *
 * @return The original [ApiResponse].
 */
@JvmSynthetic
@SuspensionFunction
suspend inline fun <T> ApiResponse<T>.suspendOnSuccess(
    crossinline onResult: suspend ApiResponse.Success<T>.() -> Unit,
): ApiResponse<T> {
    if (this is ApiResponse.Success) {
        onResult(this)
    }
    return this
}

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A scope function that would be executed for handling exception responses if the request get an exception.
 *
 * @param onResult The receiver function that receiving [ApiResponse.Failure.Exception] if the request get an exception.
 *
 * @return The original [ApiResponse].
 */
@JvmSynthetic
inline fun <T> ApiResponse<T>.onException(
    crossinline onResult: ApiResponse.Failure.Exception<T>.() -> Unit,
): ApiResponse<T> {
    if (this is ApiResponse.Failure.Exception) {
        onResult(this)
    }
    return this
}

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

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A function that would be executed for handling error responses if the request failed or get an exception.
 *
 * @param onResult The receiver function that receiving [ApiResponse.Failure] if the request failed or get an exception.
 *
 * @return The original [ApiResponse].
 */
@JvmSynthetic
inline fun <T> ApiResponse<T>.onFailure(
    crossinline onResult: ApiResponse.Failure<T>.() -> Unit,
): ApiResponse<T> {
    if (this is ApiResponse.Failure<T>) {
        onResult(this)
    }
    return this
}

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A suspension function that would be executed for handling error responses if the request failed or get an exception.
 *
 * @param onResult The receiver function that receiving [ApiResponse.Failure] if the request failed or get an exception.
 *
 * @return The original [ApiResponse].
 */
@JvmSynthetic
@SuspensionFunction
suspend inline fun <T> ApiResponse<T>.suspendOnFailure(
    crossinline onResult: suspend ApiResponse.Failure<T>.() -> Unit,
): ApiResponse<T> {
    if (this is ApiResponse.Failure<T>) {
        onResult(this)
    }
    return this
}

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A scope function that would be executed for handling error responses if the request failed.
 *
 * @param onResult The receiver function that receiving [ApiResponse.Failure.Exception] if the request failed.
 *
 * @return The original [ApiResponse].
 */
@JvmSynthetic
inline fun <T> ApiResponse<T>.onError(
    crossinline onResult: ApiResponse.Failure.Error<T>.() -> Unit,
): ApiResponse<T> {
    if (this is ApiResponse.Failure.Error) {
        onResult(this)
    }
    return this
}

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A suspension scope function that would be executed for handling error responses if the request failed.
 *
 * @param onResult The receiver function that receiving [ApiResponse.Failure.Exception] if the request failed.
 *
 * @return The original [ApiResponse].
 */
@JvmSynthetic
@SuspensionFunction
suspend inline fun <T> ApiResponse<T>.suspendOnError(
    crossinline onResult: suspend ApiResponse.Failure.Error<T>.() -> Unit,
): ApiResponse<T> {
    if (this is ApiResponse.Failure.Error) {
        onResult(this)
    }
    return this
}

/**
 * Returns an error message from the [ApiResponse.Failure] that consists of the localized message.
 *
 * @return An error message from the [ApiResponse.Failure].
 */
fun <T> ApiResponse.Failure<T>.message(): String {
    return when (this) {
        is ApiResponse.Failure.Error -> message()
        is ApiResponse.Failure.Exception -> message()
    }
}

/**
 * @author skydoves (Jaewoong Eum)
 *
 * Returns an error message from the [ApiResponse.Failure.Error] that consists of the status and error response.
 *
 * @return An error message from the [ApiResponse.Failure.Error].
 */
fun <T> ApiResponse.Failure.Error<T>.message(): String = toString()


/**
 * Returns an error message from the [ApiResponse.Failure.Exception] that consists of the localized message.
 *
 * @return An error message from the [ApiResponse.Failure.Exception].
 */
fun <T> ApiResponse.Failure.Exception<T>.message(): String = toString()

/**
 * @author skydoves (Jaewoong Eum)
 *
 * Returns a [Flow] which emits successful data if the response is a [ApiResponse.Success] and the data is not null.
 *
 * @return A coroutines [Flow] which emits successful data.
 */
@JvmSynthetic
fun <T> ApiResponse<T>.toFlow(): Flow<T> {
    return if (this is ApiResponse.Success) {
        flowOf(data)
    } else {
        emptyFlow()
    }
}

/**
 * @author skydoves (Jaewoong Eum)
 *
 * Returns a [Flow] which contains transformed data using successful data
 * if the response is a [ApiResponse.Success] and the data is not null.
 *
 * @param transformer A transformer lambda receives successful data and returns anything.
 *
 * @return A coroutines [Flow] which emits successful data.
 */
@JvmSynthetic
inline fun <T, R> ApiResponse<T>.toFlow(
    crossinline transformer: T.() -> R,
): Flow<R> {
    return if (this is ApiResponse.Success) {
        flowOf(data.transformer())
    } else {
        emptyFlow()
    }
}

/**
 * @author skydoves (Jaewoong Eum)
 *
 * Returns a [Flow] which contains transformed data using successful data
 * if the response is a [ApiResponse.Success] and the data is not null.
 *
 * @param transformer A suspension transformer lambda receives successful data and returns anything.
 *
 * @return A coroutines [Flow] which emits successful data.
 */
@JvmSynthetic
@SuspensionFunction
suspend inline fun <T, R> ApiResponse<T>.toSuspendFlow(
    crossinline transformer: suspend T.() -> R,
): Flow<R> {
    return if (this is ApiResponse.Success) {
        flowOf(data.transformer())
    } else {
        emptyFlow()
    }
}