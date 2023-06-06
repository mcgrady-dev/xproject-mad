package com.mcgrady.xproject.common.core.network.operators

import com.mcgrady.xproject.common.core.network.ApiResponse

abstract class ApiResponseSuspendOperator<T> : SandwichOperator {

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
