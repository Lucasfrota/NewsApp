package com.example.newsapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.model.NewsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository): ViewModel() {

    private var pageIndex = 1
    private var mutableHeadlines = MutableLiveData<NewsResponse>()
    val headlines: LiveData<NewsResponse>
        get() = mutableHeadlines

    fun getHeadlines(sources: String, pageSize: Int){
        viewModelScope.launch {
            newsRepository.getCachedHeadlines(sources, pageSize, pageIndex).collect{
                mutableHeadlines.value = it
            }
        }
        pageIndex++
    }
}

