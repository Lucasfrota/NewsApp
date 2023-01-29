package com.example.newsapp.util

import com.example.newsapp.data.network.model.Articles
import com.example.newsapp.presentation.model.NewsData

class Mapper {

    companion object {
        fun fromNewsAPIResponseToNewsData(articles: List<Articles>) =
            articles.map {
                NewsData(
                    it.urlToImage ?: "",
                    it.title ?: "",
                    it.description ?: "",
                    it.content ?: "",
                    it.url ?: "")
            }
    }
}