package com.sangmee.fashionpeople.ui.fragment.search.brand.result.content

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage

class SearchRecentBrandContentAdapter :
    RecyclerView.Adapter<SearchRecentBrandContentAdapter.SearchBrandContentViewHolder>() {
    private val feedImageList = mutableListOf<FeedImage>()
    var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchBrandContentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_feed_image, parent, false)
        val viewHolder = SearchBrandContentViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            onClickListener?.onClickImage(feedImageList, viewHolder.adapterPosition)
        }

        return viewHolder

    }

    override fun onBindViewHolder(holder: SearchBrandContentViewHolder, position: Int) {
        holder.bind(feedImageList[position])
    }

    override fun getItemCount(): Int = feedImageList.size

    fun setFeedImages(list: List<FeedImage>) {
        feedImageList.clear()
        feedImageList.addAll(list)
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onClickImage(feedImages: List<FeedImage>, position: Int)
    }

    inner class SearchBrandContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivFeedImage = itemView.findViewById<ImageView>(R.id.iv_feed_image)
        fun bind(feedImage: FeedImage) {

            with(itemView) {
                Glide.with(context)
                    .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${feedImage.user?.id}/feed/${feedImage.imageName}")
                    .into(ivFeedImage)
            }
        }
    }
}
