package com.example.euleritychallenge.data.api

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class petsAPIServiceTest {
    private lateinit var service: PetsAPIService
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PetsAPIService::class.java)
    }

    private fun enqueMockResponse(
        fileName: String
    ){
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockResponse.setBody(source.readString(Charsets.UTF_8))
        server.enqueue(mockResponse)
    }

    @Test
    fun getPets_sentRequest_receivedExpected(){
        runBlocking {
            enqueMockResponse("petsresponse.json")
            val responseBody = service.getPets().body()
            val request = server.takeRequest()
            assertThat(responseBody).isNotNull()
            assertThat(request.path).isEqualTo("/pets")

        }
    }

    @Test
    fun getPets_receivedResponse_correctContent(){
        runBlocking {
            enqueMockResponse("petsresponse.json")
            val responseBody = service.getPets().body()
            val articleList = responseBody!!
            val article = articleList[0]
            assertThat(article.url).isEqualTo("https://images.pexels.com/photos/1108099/pexels-photo-1108099.jpeg?format=tiny")

        }
    }


}