package com.sangmee.fashionpeople.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.retrofit.model.FeedImage

class FeedImageAdapter(
    private val customId: String
) : RecyclerView.Adapter<FeedImageAdapter.FeedImageViewHolder>() {
    private val feedImageList = mutableListOf<FeedImage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed_image, parent, false)
        val viewHolder = FeedImageViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(it.context, FeedImageDetailActivity::class.java)
            intent.putExtra(FeedImageDetailActivity.KEY_FEED_IMAGE, feedImageList[viewHolder.adapterPosition])
            parent.context.startActivity(intent)
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

    inner class FeedImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivFeedImage = itemView.findViewById<ImageView>(R.id.iv_feed_image)
        fun bind(feedImage: FeedImage) {

            with(itemView) {
                Glide.with(context)
                    .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/$customId/feed/${feedImage.imageName}")
                    .into(ivFeedImage)
            }
        }
    }
}
