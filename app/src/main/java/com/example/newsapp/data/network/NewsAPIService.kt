package com.example.newsapp.data.network

import com.example.newsapp.BuildConfig
import com.example.newsapp.data.network.model.NewsAPIResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPIService {

    @GET("top-headlines")
    suspend fun searchNews(
        @Query("sources") sources: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): NewsAPIResponse
}