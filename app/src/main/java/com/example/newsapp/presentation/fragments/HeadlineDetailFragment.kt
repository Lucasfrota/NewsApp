package com.example.newsapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentHeadlineDetailBinding
import com.example.newsapp.presentation.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeadlineDetailFragment : Fragment() {

    private val viewModel: NewsViewModel by activityViewModels()
    private lateinit var binding: FragmentHeadlineDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.getHeadlineDetails(it.getInt("position"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_headline_detail,
            container,
            false)

        viewModel.headLineDetail.observe(viewLifecycleOwner) {
            Glide.with(binding.headlineImage.context)
                .load(it.urlToImage)
                .into(binding.headlineImage)
            binding.apply {
                title = it.title
                description = it.description
                content = it.content
            }
        }

        return binding.root
    }
}