package com.example.newsapp.util

import com.example.newsapp.data.network.model.Articles
import com.example.newsapp.data.network.model.Source
import org.junit.Test
import java.util.*

internal class MapperTest{

    private val articlesWithData = listOf(
        Articles(
            Source("mockId", "mockName"),
            "mockAuthor",
            "mockTitle",
            "mockDescription",
            "mockUrl",
            "mockUrlToImage",
            Date(),
            "mockContent",
        ),
        Articles(
            Source("mockId", "mockName"),
            "mockAuthor1",
            "mockTitle1",
            "mockDescription1",
            "mockUrl1",
            "mockUrlToImage1",
            Date(),
            "mockContent1",
        )
    )

    private val emptyArticles = listOf(
        Articles(), Articles()
    )

    @Test
    fun fromNewsAPIResponseToNewsData_articlesWithData_correctNewsData(){
        val result = Mapper.fromNewsAPIResponseToNewsData(articlesWithData)
        assert(result[0].title == articlesWithData[0].title)
        assert(result[1].title == articlesWithData[1].title)
    }

    @Test
    fun fromNewsAPIResponseToNewsData_emptyArticles_correctNewsData(){
        val result = Mapper.fromNewsAPIResponseToNewsData(emptyArticles)
        assert(result[0].title == "")
    }

}