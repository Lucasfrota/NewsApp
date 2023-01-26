package com.example.newsapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.BuildConfig
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentHeadlinesBinding
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.presentation.adapters.HeadlinesAdapter
import com.example.newsapp.presentation.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

const val newsSourceId = BuildConfig.NEWS_SOURCE_ID
const val newsSourceName = BuildConfig.NEWS_SOURCE_NAME
const val pageSize = 20

@AndroidEntryPoint
class HeadlinesFragment : Fragment() {

    private val viewModel: NewsViewModel by activityViewModels()
    private lateinit var binding: FragmentHeadlinesBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var headlinesAdapter: HeadlinesAdapter

    private var isLoading = false
    private var isFinalPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.title = newsSourceName
        viewModel.getHeadlines(newsSourceId, pageSize)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_headlines,
            container,
            false
        )

        initRecyclerView()

        viewModel.headlines.observe(viewLifecycleOwner) {
            when(it){
                is NewsResponse.Success -> {
                    isLoading = false
                    headlinesAdapter.differ.submitList(it.newsData)
                    headlinesAdapter.notifyItemRangeChanged(it.newsData.size-pageSize, pageSize)
                    if(it.newsData.size == it.totalResult){
                        isFinalPage = true
                    }
                }
                is NewsResponse.Error -> {
                    isLoading = false
                }
                is NewsResponse.Loading -> {
                    isLoading = true
                }
            }
        }

        return binding.root
    }

    private fun initRecyclerView(){
        linearLayoutManager = LinearLayoutManager(activity)
        headlinesAdapter = HeadlinesAdapter()
        binding.layoutManager = linearLayoutManager
        binding.headlinesRecyclerView.addOnScrollListener(onScrollListener)
        binding.headlinesAdapter = headlinesAdapter
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1) && !isLoading && !isFinalPage) {
                isLoading = true
                viewModel.getHeadlines(newsSourceId, pageSize)
            }
        }
    }
}