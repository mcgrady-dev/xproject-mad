package com.mcgrady.xproject.core.network.interceptors

import com.mcgrady.xproject.core.network.StatusCode
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import com.mcgrady.xproject.core.network.exceptions.NoContentException

/**
 * Related: https://github.com/square/retrofit/issues/2867
 *
 * An interceptor for bypassing the [NoContentException]
 * when the server has successfully fulfilled the request with the 2xx code
 * and that there is no additional content to send in the response payload body.
 * e.g., 204 (NoContent), 205 (ResetContent).
 */
object EmptyBodyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (!response.isSuccessful || response.code.let { it != StatusCode.NoContent.code && it != StatusCode.ResetContent.code }) {
            return response
        }

        if ((response.body?.contentLength()?.takeIf { it >= 0 } != null)) {
            return response.newBuilder().code(StatusCode.OK.code).build()
        }

        val emptyBody = "".toResponseBody("text/plain".toMediaType())

        return response
            .newBuilder()
            .code(StatusCode.OK.code)
            .body(emptyBody)
            .build()
    }
}
