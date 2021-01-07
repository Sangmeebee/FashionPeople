package com.sangmee.fashionpeople.ui.fragment.home.evaluate

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.ItemEvaluateFeedBinding

class EvaluateFeedViewHolder(private val binding: ItemEvaluateFeedBinding, private val myId: String) :
    RecyclerView.ViewHolder(binding.root) {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun bind(feedImage: FeedImage) {
        binding.myId = myId
        binding.feedImage = feedImage
        with(itemView) {
            Glide.with(context)
                .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${feedImage.user?.id}/feed/${feedImage.imageName}")
                .placeholder(context.getDrawable(R.drawable.white_image))
                .into(binding.ivItemEvaluateFeed)

            if(feedImage.user?.profileImage.isNullOrEmpty()){
                binding.ivProfileEvaluateFeed.setImageDrawable(context.getDrawable(R.drawable.ic_person_black))
            } else{
                Glide.with(context)
                    .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${feedImage.user?.id}/profile/${feedImage.user?.profileImage}")
                    .error(context.getDrawable(R.drawable.ic_person_black))
                    .placeholder(context.getDrawable(R.drawable.ic_person_black))
                    .into(binding.ivProfileEvaluateFeed)
            }
        }
    }
}
