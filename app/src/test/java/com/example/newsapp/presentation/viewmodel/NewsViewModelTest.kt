package com.example.newsapp.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.model.NewsData
import com.example.newsapp.model.NewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

internal class NewsViewModelTest{

    private val newsData = NewsData(
        "mockUrlImage",
        "mockTitle",
        "mockDescription",
        "mockContent",
        "mockUrl"
    )

    private val firstPageFlow = flow {
        emit(NewsResponse.Success(
            listOf(
                newsData
            ),
            2
        ))
    }

    private val finalPageFlow = flow {
        emit(NewsResponse.Success(
            listOf(
                newsData,
                newsData
            ),
            2
        ))
    }

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsRepository: NewsRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        newsRepository = mock {
            onBlocking { getCachedHeadlines("bbc-news", 1, 1) } doReturn firstPageFlow
            onBlocking { getCachedHeadlines("bbc-news", 1, 2) } doReturn finalPageFlow
        }
        newsViewModel = NewsViewModel(newsRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun newsViewModel_pagination_increaseNewsLiveData() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        newsViewModel.getHeadlines("bbc-news", 1)
        val firstPageResult = newsViewModel.headlines.value
        newsViewModel.getHeadlines("bbc-news", 1)
        val secondPageResult = newsViewModel.headlines.value

        assert(firstPageResult!!.newsData.size == 1)
        assert(secondPageResult!!.newsData.size == 2)

        Dispatchers.resetMain()
    }
}