package com.sangmee.fashionpeople.ui.fragment.rank.content

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.model.RankImage
import com.sangmee.fashionpeople.databinding.ItemRankContentBinding
import kotlinx.android.synthetic.main.item_rank_content.view.*

class RankRecyclerAdapter(private val callDetailFragment: (List<FeedImage>, Int) -> Unit) :
    RecyclerView.Adapter<RankRecyclerAdapter.RankContentViewHolder>() {

    private val items = mutableListOf<RankImage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankContentViewHolder {
        val binding = DataBindingUtil.inflate<ItemRankContentBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_rank_content,
            parent,
            false
        )

        val viewHolder = RankContentViewHolder(binding)

        viewHolder.itemView.iv_rank.setOnClickListener {
            val feedImages = mutableListOf<FeedImage>()
            for (rankImage in items) {
                feedImages.add(rankImage.feedImage!!)
            }
            callDetailFragment(feedImages, viewHolder.adapterPosition)

        }

        return viewHolder
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
            binding.position = adapterPosition
            Glide.with(itemView.context)
                .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${item.feedImage?.user?.id}/feed/${item.feedImage?.imageName}")
                .into(binding.ivRank)
        }
    }
}
