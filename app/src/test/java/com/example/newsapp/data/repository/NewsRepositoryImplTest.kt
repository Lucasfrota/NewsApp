package com.example.newsapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.newsapp.data.cache.HeadlinesCache
import com.example.newsapp.data.network.NewsAPIService
import com.example.newsapp.data.network.model.Articles
import com.example.newsapp.data.network.model.NewsAPIResponse
import com.example.newsapp.data.network.model.Source
import com.example.newsapp.model.NewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.junit.*
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import retrofit2.Response
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
internal class NewsRepositoryImplTest{

    private val firstArticle = Articles(
        Source("mockId", "mockName"),
        "mockAuthor1",
        "mockTitle1",
        "mockDescription1",
        "mockUrl1",
        "mockUrlToImage1",
        Date(),
        "mockContent1",
    )

    private val secondArticle = Articles(
        Source("mockId", "mockName"),
        "mockAuthor2",
        "mockTitle2",
        "mockDescription2",
        "mockUrl2",
        "mockUrlToImage2",
        Date(),
        "mockContent2",
    )

    private val newsAPIResponseNoError = NewsAPIResponse(
        "ok",
        10,
        arrayListOf(
            firstArticle,
            secondArticle
        )
    )
    private val newsAPIResponseError = NewsAPIResponse(
        "error"
    )

    private lateinit var newsAPIService: NewsAPIService
    private lateinit var headlinesCache: HeadlinesCache
    private lateinit var newsRepositoryImpl: NewsRepositoryImpl

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        newsAPIService = mock {
            onBlocking {
                searchNews("bbc-news", 1, 1)
            } doReturn newsAPIResponseNoError
            onBlocking {
                searchNews("bbc-news", 1, 2)
            } doReturn newsAPIResponseNoError
            onBlocking {
                searchNews("bbc-news", 1, 3)
            } doReturn newsAPIResponseError
        }
        headlinesCache = HeadlinesCache()
        newsRepositoryImpl = NewsRepositoryImpl(newsAPIService, headlinesCache)
    }

    private fun setTestDispatcher(testScope: TestScope){
        val testDispatcher = UnconfinedTestDispatcher(testScope.testScheduler)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun getCachedHeadlines_okResponse_loadingOnStart() = runTest{
        setTestDispatcher(this)

        val firstResult = newsRepositoryImpl
            .getCachedHeadlines("bbc-news", 1, 1).first()

        assert(firstResult is NewsResponse.Loading)

        Dispatchers.resetMain()
    }

    @Test
    fun getCachedHeadlines_okResponse_increaseCache() = runTest{
        setTestDispatcher(this)

        val firstResult = newsRepositoryImpl
            .getCachedHeadlines("bbc-news", 1, 1)
            .drop(1)
            .first()
            .newsData
            .size
        val finalResult = newsRepositoryImpl
            .getCachedHeadlines("bbc-news", 1, 2)
            .drop(1)
            .first()
            .newsData
            .size

        assert(firstResult == 2)
        assert(finalResult == 4)

        Dispatchers.resetMain()
    }

    @Test
    fun getCachedHeadlines_errorResponse_emitError() = runTest{
        setTestDispatcher(this)

        val firstResult = newsRepositoryImpl
            .getCachedHeadlines("bbc-news", 1, 3).drop(1).first()

        assert(firstResult is NewsResponse.Error)

        Dispatchers.resetMain()
    }

    @Test
    fun getCachedHeadlineDetails_okResponse_getDetail() = runTest{
        setTestDispatcher(this)

        newsRepositoryImpl
            .getCachedHeadlines("bbc-news", 1, 1)
            .drop(1)
            .first()

        val result = newsRepositoryImpl.getCachedHeadlineDetails(1)

        assert(result.title == secondArticle.title)

        Dispatchers.resetMain()
    }

}