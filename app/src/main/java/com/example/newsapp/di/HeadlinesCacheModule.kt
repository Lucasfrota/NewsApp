package com.example.newsapp.di

import com.example.newsapp.data.cache.HeadlinesCache
import com.example.newsapp.data.cache.HeadlinesCacheImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HeadlinesCacheModule {
    @Binds
    @Singleton
    abstract fun bindsHeadlinesCache(headlinesCacheImpl: HeadlinesCacheImpl): HeadlinesCache
}