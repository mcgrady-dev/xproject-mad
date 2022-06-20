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
package com.mcgrady.xproject.pokemon

import com.google.common.truth.Truth
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by mcgrady on 2022/6/8.
 */
class MockWebServerTest {

    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After
    fun clearUp() {
        server.shutdown()
    }

    @Test
    fun defaultMockResponse() {
        val response = MockResponse()
        Truth.assertThat(response.headers.toString()).contains("Content-Length: 0")
        Truth.assertThat(response.status).isEqualTo("HTTP/1.1 200 OK")
    }

    @Test
    fun setResponseMockReason() {
        val reasons = arrayOf(
            "Mock Response",
            "Informational",
            "OK",
            "Redirection",
            "Client Error",
            "Server Error",
            "Mock Response"
        )
        for (i in 0..600) {
            val response = MockResponse().apply {
                setResponseCode(i)
            }
            val expectedReason = reasons[i / 100]
            Truth.assertThat((response.status)).isEqualTo(("HTTP/1.1 $i $expectedReason"))
            Truth.assertThat(response.headers.toString()).contains("Content-Length: 0")
        }
    }
}
