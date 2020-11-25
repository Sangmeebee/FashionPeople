package com.sangmee.fashionpeople.ui.fragment.rank

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.data.model.RankImage
import com.sangmee.fashionpeople.databinding.ItemRankContentBinding

class RankContentViewHolder(
    val binding: ItemRankContentBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RankImage, rank: Int) {
        binding.rank = rank
        Glide.with(itemView.context)
            .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${item.feedImage?.user?.id}/feed/${item.feedImage?.imageName}")
            .into(binding.ivRank)
    }
}