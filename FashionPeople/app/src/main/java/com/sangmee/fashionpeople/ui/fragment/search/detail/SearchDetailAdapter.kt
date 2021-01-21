package com.sangmee.fashionpeople.ui.fragment.search.detail

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.ItemInfoDetailFeedBinding
import kotlinx.android.synthetic.main.item_info_detail_feed.view.*


class SearchDetailAdapter : RecyclerView.Adapter<SearchDetailAdapter.DetailViewHolder>() {

    private val items = mutableListOf<FeedImage>()
    private val saveItems = mutableListOf<String>()
    var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val binding = DataBindingUtil.inflate<ItemInfoDetailFeedBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_info_detail_feed,
            parent,
            false
        )
        val viewHolder = DetailViewHolder(binding)

        viewHolder.itemView.iv_comment.setOnClickListener {
            items[viewHolder.adapterPosition].let {
                it.imageName?.let { imageName ->
                    onClickListener?.onClickComment(imageName)
                }
            }
        }

        viewHolder.itemView.iv_save_image.setOnClickListener {
            items[viewHolder.adapterPosition].let {
                it.imageName?.let { imageName ->
                    onClickListener?.onClickSave(imageName, viewHolder.adapterPosition)
                }
            }
        }

        viewHolder.itemView.iv_delete_image.setOnClickListener {
            items[viewHolder.adapterPosition].let {
                it.imageName?.let { imageName ->
                    onClickListener?.onClickDelete(imageName, viewHolder.adapterPosition)
                }
            }
        }

        viewHolder.itemView.ll_rating_evaluate_average.setOnClickListener {
            items[viewHolder.adapterPosition].let {
                onClickListener?.onClickGrade(it)
            }
        }

        viewHolder.itemView.civ_profile.setOnClickListener {
            items[viewHolder.adapterPosition].let {
                onClickListener?.onClickProfile(it)
            }
        }


        viewHolder.itemView.ll_tag.setOnClickListener {
            items[viewHolder.adapterPosition].let {
                onClickListener?.onClickTag(it)
            }
        }

        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
            holder.bind(items[position], saveItems)

    }

    override fun getItemCount(): Int = items.size


    fun setFeedImages(list: List<FeedImage>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun setSaveItems(list: List<String>, position: Int?) {
        saveItems.clear()
        saveItems.addAll(list)
        Log.d("SangmeePosition", position.toString())
        if (position == null) {
            notifyDataSetChanged()
        } else {
            notifyItemChanged(position)
        }
    }

    interface OnClickListener {
        fun onClickComment(imageName: String)
        fun onClickGrade(feedImage: FeedImage)
        fun onClickProfile(feedImage: FeedImage)
        fun onClickSave(imageName: String, position: Int)
        fun onClickDelete(imageName: String, position: Int)
        fun onClickTag(feedImage: FeedImage)
    }

    class DetailViewHolder(private val binding: ItemInfoDetailFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bind(feedImage: FeedImage, saveItems : List<String>) {
            binding.feedImage = feedImage
            binding.isSaved = feedImage.imageName in saveItems
            binding.executePendingBindings()


            with(itemView) {
                Glide.with(context)
                    .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${feedImage.user?.id}/feed/${feedImage.imageName}")
                    .into(binding.ivItemEvaluateFeed)

                if (feedImage.user?.profileImage.isNullOrEmpty()) {
                    binding.civProfile.setImageDrawable(context.getDrawable(R.drawable.ic_profile_home))
                } else {
                    Glide.with(context)
                        .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${feedImage.user?.id}/profile/${feedImage.user?.profileImage}")
                        .error(context.getDrawable(R.drawable.ic_profile_home))
                        .placeholder(context.getDrawable(R.drawable.ic_profile_home))
                        .into(binding.civProfile)
                }
            }
        }
    }


}
