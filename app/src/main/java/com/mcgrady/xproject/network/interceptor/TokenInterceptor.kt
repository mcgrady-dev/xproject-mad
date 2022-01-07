package com.mcgrady.xproject.network.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by mcgrady on 2021/12/15.
 */
class TokenInterceptor : Interceptor {

    companion object {

        const val RESPONSE_CODE_TOKEN_EXPIRED = 210
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)

        if (isTokenExpired(response)) {

            //val token: String = call.execute().body()
            val token = ""
            val newRequest = chain.request()
                .newBuilder()
                .header("x-access-token", token)
                .build()
            return chain.proceed(newRequest)
        }

        return response
    }

    private fun isTokenExpired(response: Response): Boolean {
        return response.code == RESPONSE_CODE_TOKEN_EXPIRED
    }
}