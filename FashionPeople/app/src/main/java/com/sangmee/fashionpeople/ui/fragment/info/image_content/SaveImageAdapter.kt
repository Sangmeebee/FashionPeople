package com.sangmee.fashionpeople.ui.fragment.info.image_content

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.info.detail.InfoDetailFragment

class SaveImageAdapter(
    private val customId: String
) : RecyclerView.Adapter<SaveImageAdapter.SaveImageViewHolder>() {
    private val saveImageList = mutableListOf<FeedImage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_save_image, parent, false)
        val viewHolder = SaveImageViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            (parent.context as MainActivity).replaceFragmentUseBackStack(
                InfoDetailFragment(
                    customId,
                    viewHolder.adapterPosition,
                    1
                )
            )
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

    inner class SaveImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
