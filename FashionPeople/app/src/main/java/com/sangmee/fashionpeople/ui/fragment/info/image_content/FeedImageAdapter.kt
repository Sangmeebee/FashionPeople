package com.sangmee.fashionpeople.ui.fragment.info.image_content

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.ItemFeedImageExBinding
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.info.detail.InfoDetailFragment
import com.sangmee.fashionpeople.util.getRatingFromEvaluations
import java.lang.Math.round

class FeedImageAdapter(
    private val customId: String
) : RecyclerView.Adapter<FeedImageAdapter.FeedImageViewHolder>() {
    private val feedImageList = mutableListOf<FeedImage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedImageViewHolder {
        val binding = DataBindingUtil.inflate<ItemFeedImageExBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_feed_image_ex,
            parent,
            false
        )
        binding.noSelectStr = "..."
        val viewHolder = FeedImageViewHolder(binding)
        viewHolder.itemView.setOnClickListener {

            (parent.context as MainActivity).replaceFragmentUseBackStack(
                InfoDetailFragment(
                    customId,
                    viewHolder.adapterPosition,
                    0
                )
            )
        }

        return viewHolder

    }

    override fun onBindViewHolder(holder: FeedImageViewHolder, position: Int) {
        holder.bind(feedImageList[position])
    }

    override fun getItemCount(): Int = feedImageList.size

    fun setFeedImages(list: List<FeedImage>) {
        feedImageList.clear()
        feedImageList.addAll(list)
        notifyDataSetChanged()
    }

    inner class FeedImageViewHolder(private val binding: ItemFeedImageExBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val ivFeedImage = itemView.findViewById<ImageView>(R.id.iv_feed_image)
        fun bind(feedImage: FeedImage) {

            binding.isSelectedStyle = !feedImage.style.isNullOrEmpty()
            binding.isSelectedTop = !feedImage.top.isNullOrEmpty()
            binding.isSelectedPants = !feedImage.pants.isNullOrEmpty()
            binding.isSelectedShoes = !feedImage.shoes.isNullOrEmpty()
            var average = 0f
                feedImage.evaluations?.let {
                    average = getRatingFromEvaluations(it)
                }
            val resultScore = String.format("%.1f", average)
            binding.resultScore = resultScore

            binding.feedImage = feedImage

            with(itemView) {
                Glide.with(context)
                    .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/$customId/feed/${feedImage.imageName}")
                    .into(ivFeedImage)
            }
        }
    }
}
