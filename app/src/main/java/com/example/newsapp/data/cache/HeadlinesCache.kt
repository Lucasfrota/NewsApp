package com.example.newsapp.data.cache

import com.example.newsapp.model.NewsData
import javax.inject.Inject
import javax.inject.Singleton

class HeadlinesCache @Inject constructor() {

    @Singleton
    val headlines: MutableList<NewsData> = arrayListOf()

    fun addHeadlines(newHeadlines: List<NewsData>): List<NewsData> {
        headlines += newHeadlines
        return headlines
    }

}