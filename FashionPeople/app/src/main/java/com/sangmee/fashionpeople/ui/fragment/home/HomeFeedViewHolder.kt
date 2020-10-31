package com.sangmee.fashionpeople.ui.fragment.home

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.databinding.ItemHomeFeedBinding
import com.sangmee.fashionpeople.retrofit.model.FeedImage

class HomeFeedViewHolder(private val binding: ItemHomeFeedBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(feedImage: FeedImage) {
        with(itemView) {
            Glide.with(context)
                .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${feedImage.user?.id}/feed/${feedImage.imageName}")
                .into(binding.ivItemHomeFeed)

        }
    }
}