package com.sangmee.fashionpeople.ui.fragment.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.ItemHomeFeedBinding
import com.sangmee.fashionpeople.retrofit.model.FeedImage

class HomeFeedAdapter: RecyclerView.Adapter<HomeFeedViewHolder>() {

    private val items = mutableListOf<FeedImage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFeedViewHolder {
        val binding = DataBindingUtil.inflate<ItemHomeFeedBinding>(LayoutInflater.from(parent.context), R.layout.item_home_feed, parent, false)
        val viewHolder = HomeFeedViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: HomeFeedViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size


}