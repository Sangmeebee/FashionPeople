package com.sangmee.fashionpeople.ui.fragment.rank.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.RankImage
import com.sangmee.fashionpeople.databinding.ItemRankContentBinding

class RankRecyclerAdapter : RecyclerView.Adapter<RankRecyclerAdapter.RankContentViewHolder>() {

    private val items = mutableListOf<RankImage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankContentViewHolder {
        val binding = DataBindingUtil.inflate<ItemRankContentBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_rank_content,
            parent,
            false
        )

        return RankContentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankContentViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    fun setRankImages(list: List<RankImage>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }


    class RankContentViewHolder(
        val binding: ItemRankContentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RankImage, rank: Int) {
            binding.rank = rank
            binding.rankImage = item
            Glide.with(itemView.context)
                .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${item.feedImage?.user?.id}/feed/${item.feedImage?.imageName}")
                .into(binding.ivRank)
        }
    }
}
