package com.example.newsapp.data.network

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


internal class NewsAPIServiceTest {

    private lateinit var service: NewsAPIService
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp(){
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsAPIService::class.java)
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }

    private fun enqueueMockResponse(file: String){
        val inputStream = javaClass.classLoader!!.getResourceAsStream(file)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockResponse.setBody(source.readString(Charsets.UTF_8))
        mockWebServer.enqueue(mockResponse)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun searchNews_sentRequest_receiveExpectedResult() = runTest{
        enqueueMockResponse("NewsAPIResponse.json")
        val responseBody = service.searchNews("bbc-news", 20, 1)
        val request = mockWebServer.takeRequest()
        assert(responseBody.status == "ok")
        assert(responseBody.status == "ok")
        assert(request.path == "/top-headlines?sources=bbc-news&pageSize=20&page=1&apiKey=ad4949ebe6294ddd9c8adb03c75a73a9")
    }

}