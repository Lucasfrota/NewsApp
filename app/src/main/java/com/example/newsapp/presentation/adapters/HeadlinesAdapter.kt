package com.example.newsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.HeadlineItemBinding
import com.example.newsapp.model.NewsData

class HeadlinesAdapter(
    private val onClick: (position: Int) -> Unit
): RecyclerView.Adapter<HeadlinesAdapter.ViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<NewsData>(){
        override fun areItemsTheSame(oldItem: NewsData, newItem: NewsData) =
            oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: NewsData, newItem: NewsData) =
            newItem == oldItem
    }

    val differ = AsyncListDiffer(this, callback)

    class ViewHolder(
        val binding: HeadlineItemBinding
        ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binder = DataBindingUtil.inflate<HeadlineItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.headline_item,
            parent,
            false
        )
        return ViewHolder(binder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.binding.headlineTitle = item.title
        Glide.with(holder.binding.imageView.context)
            .load(item.urlToImage)
            .into(holder.binding.imageView)
        holder.binding.root.setOnClickListener { onClick(position) }
    }

    override fun getItemCount() = differ.currentList.size
}