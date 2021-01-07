package com.sangmee.fashionpeople.ui.fragment.home.following

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.ItemFollowingFeedBinding

class FollowingFeedViewHolder(
    private val binding: ItemFollowingFeedBinding,
    private val myId: String
) :
    RecyclerView.ViewHolder(binding.root) {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun bind(feedImage: FeedImage) {

        var canEvaluate = false

        if (feedImage.resultTimeStamp.isNullOrEmpty()) {
            feedImage.evaluations?.let { evaluations ->
                var isExist = false
                for (evaluation in evaluations) {
                    if (evaluation.evaluationPersonId == myId) {
                        isExist = true
                        break
                    }
                }
                if (!isExist) {
                    canEvaluate = true
                }
            }
        }
        binding.canEvaluate = canEvaluate
        binding.feedImage = feedImage
        with(itemView) {
            Glide.with(context)
                .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${feedImage.user?.id}/feed/${feedImage.imageName}")
                .into(binding.ivItemFollowingFeed)

            if (feedImage.user?.profileImage.isNullOrEmpty()) {
                binding.ivProfileFollowingFeed.setImageDrawable(context.getDrawable(R.drawable.ic_person_black))
            } else {
                Glide.with(context)
                    .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${feedImage.user?.id}/profile/${feedImage.user?.profileImage}")
                    .error(context.getDrawable(R.drawable.ic_person_black))
                    .placeholder(context.getDrawable(R.drawable.ic_person_black))
                    .into(binding.ivProfileFollowingFeed)
            }
        }
    }
}
