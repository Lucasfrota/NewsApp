package com.example.newsapp.data.cache

import com.example.newsapp.model.NewsData

interface HeadlinesCache {

    fun addHeadlines(newHeadlines: List<NewsData>): List<NewsData>

    fun getHeadlineByPosition(position: Int): NewsData

}