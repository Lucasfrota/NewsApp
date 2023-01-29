package com.example.newsapp.data.cache

import com.example.newsapp.presentation.model.NewsData
import org.junit.Before
import org.junit.Test

internal class HeadlinesCacheImplTest{

    private val newsData1 = NewsData(
        "urlToImage1",
        "title1",
        "description1",
        "content1",
        "url1"
    )
    private val newsData2 = NewsData(
        "urlToImage2",
        "title2",
        "description2",
        "content2",
        "url12"
    )

    private lateinit var headlinesCacheImpl: HeadlinesCacheImpl

    @Before
    fun setUp(){
        headlinesCacheImpl = HeadlinesCacheImpl()
    }

    @Test
    fun getHeadlineByPosition_addNews_getCorrectSize(){
        headlinesCacheImpl.addHeadlines(listOf(newsData1))
        headlinesCacheImpl.addHeadlines(listOf(newsData2))
        val cache = headlinesCacheImpl.getHeadlineByPosition(1)

        assert(cache.title == newsData2.title)
    }

}