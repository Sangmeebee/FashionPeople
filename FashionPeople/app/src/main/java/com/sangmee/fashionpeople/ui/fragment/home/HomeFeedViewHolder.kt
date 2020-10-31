package com.sangmee.fashionpeople.ui.fragment.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.ItemHomeFeedBinding
import com.sangmee.fashionpeople.retrofit.model.FeedImage

class HomeFeedViewHolder(private val binding: ItemHomeFeedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun bind(feedImage: FeedImage) {
        binding.feedImage = feedImage
        with(itemView) {
            Glide.with(context)
                .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${feedImage.user?.id}/feed/${feedImage.imageName}")
                .into(binding.ivItemHomeFeed)

            Glide.with(context)
                .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${feedImage.user?.id}/profile/${feedImage.user?.profileImage}")
                .error(context.getDrawable(R.drawable.ic_person_white))
                .placeholder(context.getDrawable(R.drawable.ic_person_white))
                .circleCrop()
                .into(binding.ivProfileHomeFeed)
        }
    }
}