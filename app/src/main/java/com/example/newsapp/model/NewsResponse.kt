package com.example.newsapp.model

sealed class NewsResponse(val newsData: List<NewsData> = emptyList(), val totalResult: Int = 0){
    class Success(newsData: List<NewsData>, totalResult: Int): NewsResponse(newsData, totalResult)
    class Error: NewsResponse()
    class Loading: NewsResponse()
}
