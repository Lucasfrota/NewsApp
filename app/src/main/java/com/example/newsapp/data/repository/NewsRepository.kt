package com.example.newsapp.data.repository

import com.example.newsapp.model.NewsResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getCachedHeadlines(sources: String, pageSize: Int, page: Int): Flow<NewsResponse>
}