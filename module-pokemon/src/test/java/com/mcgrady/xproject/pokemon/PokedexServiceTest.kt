package com.mcgrady.xproject.pokemon

import com.mcgrady.xproject.pokemon.network.PokedexService
import com.mcgrady.xproject.pokemon.network.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import com.mcgrady.xproject.pokemon.model.PokemonResponse
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory(contentType))
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