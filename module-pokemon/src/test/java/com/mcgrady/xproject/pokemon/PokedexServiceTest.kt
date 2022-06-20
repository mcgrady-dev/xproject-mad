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

import com.mcgrady.xproject.pokemon.model.PokemonResponse
import com.mcgrady.xproject.pokemon.network.PokedexService
import com.mcgrady.xproject.pokemon.network.asConverterFactory
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit

/**
 * Created by mcgrady on 2022/6/8.
 */
class PokedexServiceTest {

    @Rule
//    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: PokedexService
    private lateinit var server: MockWebServer

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Before
    fun setUp() {
        server = MockWebServer()

        val contentType = "application/json".toMediaType()

        service = Retrofit.Builder()
            .baseUrl(server.url("https://pokeapi.co/api/v2/"))
            .addConverterFactory(
                Json {
                    ignoreUnknownKeys = true
                }.asConverterFactory(contentType)
            )
            .build()
            .create(PokedexService::class.java)
    }

    @After
    fun clearUp() {
        server.shutdown()
    }

    fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader?.getResourceAsStream("api-response/$fileName")
        val bufferedSource = inputStream?.source()?.buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        server.enqueue(mockResponse.setBody(bufferedSource?.readString(Charsets.UTF_8) ?: ""))
    }

    @Test
    fun getFakeUserSuccess() = runBlocking {
        enqueueResponse("get_user.json")

        val response: ApiResponse<PokemonResponse> = service.fetchPokemonList()
//        Assert.assertTrue(response.isSuccess())
//        TestCase.assertEquals(response.data, FakeUser("LiHua", "male"))
//
//        val resource =
//            response.toResource { if (it.data == null) Resource.empty() else Resource.success(it.data) }
//        assertEquals(resource, Resource.success(response.data))
    }
}
