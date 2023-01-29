package com.example.newsapp.data.repository

import com.example.newsapp.data.network.NewsAPIService
import com.example.newsapp.data.cache.HeadlinesCache
import com.example.newsapp.presentation.model.NewsData
import com.example.newsapp.util.Mapper
import com.example.newsapp.presentation.model.NewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
        private val newsAPIService: NewsAPIService,
        private val headlinesCache: HeadlinesCache
    ): NewsRepository {
    override suspend fun getCachedHeadlines(sources: String, pageSize: Int, page: Int): Flow<NewsResponse> = flow {
        emit(NewsResponse.Loading())
        val response = newsAPIService.searchNews(sources, pageSize, page)
        if(response.status.equals("ok")){
            val headlines = Mapper.fromNewsAPIResponseToNewsData(response.articles)
            val cachedHeadlines = headlinesCache.addHeadlines(headlines)
            emit(NewsResponse.Success(cachedHeadlines, response.totalResults))
        }else{
            emit(NewsResponse.Error())
        }
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(NewsResponse.Error())
        }

    override fun getCachedHeadlineDetails(position: Int): NewsData {
        return headlinesCache.getHeadlineByPosition(position)
    }
}