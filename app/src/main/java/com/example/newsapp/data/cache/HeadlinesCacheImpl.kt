package com.example.newsapp.data.cache

import com.example.newsapp.presentation.model.NewsData
import javax.inject.Inject

class HeadlinesCacheImpl @Inject constructor(): HeadlinesCache{

    private val headlines: MutableList<NewsData> = arrayListOf()

    override fun addHeadlines(newHeadlines: List<NewsData>): List<NewsData> {
        headlines += newHeadlines
        return headlines
    }

    override fun getHeadlineByPosition(position: Int): NewsData {
        return headlines[position]
    }

}