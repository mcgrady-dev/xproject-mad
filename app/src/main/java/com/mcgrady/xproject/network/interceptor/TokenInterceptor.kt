/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            // val token: String = call.execute().body()
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
