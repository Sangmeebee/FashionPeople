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
import com.sangmee.fashionpeople.util.getRatingFromEvaluations

class SaveImageAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<SaveImageAdapter.SaveImageViewHolder>() {
    private val saveImageList = mutableListOf<FeedImage>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SaveImageAdapter.SaveImageViewHolder {
        val binding = DataBindingUtil.inflate<ItemFeedImageExBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_feed_image_ex,
            parent,
            false
        )
        binding.noSelectStr = "선택안함"
        val viewHolder = SaveImageViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            onClickListener.onClickItem(saveImageList, viewHolder.adapterPosition)
        }

        return viewHolder

    }

    override fun onBindViewHolder(holder: SaveImageViewHolder, position: Int) {
        holder.bind(saveImageList[position])
    }

    override fun getItemCount(): Int = saveImageList.size

    fun setFeedImages(list: List<FeedImage>) {
        saveImageList.clear()
        saveImageList.addAll(list)
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onClickItem(images: List<FeedImage>, position: Int)
    }

    inner class SaveImageViewHolder(private val binding: ItemFeedImageExBinding) :
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
                    .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${feedImage.user?.id}/feed/${feedImage.imageName}")
                    .into(ivFeedImage)
            }
        }
    }
}
