package com.mcgrady.xproject.common.core.network.operators

import com.mcgrady.xproject.common.core.network.ApiResponse

/**
 * @author skydoves (Jaewoong Eum)
 *
 * ApiResponseSuspendOperator operates on an [ApiResponse] which should be handled in the suspension scope.
 * This allows you to handle success and error response instead of the [com.mcgrady.xproject.common.core.network.suspendOnSuccess],
 * [com.skydoves.sandwich.suspendOnError], [com.skydoves.sandwich.suspendOnException] transformers.
 * This operator can be applied globally as a singleton instance, or on each [ApiResponse] one by one.
 */
abstract class ApiResponseSuspendOperator<T> : IResponseOperator {

  /**
   * Operates the [ApiResponse.Success] for handling successful responses if the request succeeds.
   *
   * @param apiResponse The successful response.
   */
  abstract suspend fun onSuccess(apiResponse: ApiResponse.Success<T>)

  /**
   * Operates the [ApiResponse.Failure.Error] for handling error responses if the request failed.
   *
   * @param apiResponse The failed response.
   */
  abstract suspend fun onError(apiResponse: ApiResponse.Failure.Error<T>)

  /**
   * Operates the [ApiResponse.Failure.Exception] for handling exception responses if the request get an exception.
   *
   * @param apiResponse The exception response.
   */
  abstract suspend fun onException(apiResponse: ApiResponse.Failure.Exception<T>)
}
