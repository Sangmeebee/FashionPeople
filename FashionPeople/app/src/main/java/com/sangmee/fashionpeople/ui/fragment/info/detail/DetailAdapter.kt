package com.sangmee.fashionpeople.ui.fragment.info.detail

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.ItemInfoDetailFeedBinding
import kotlinx.android.synthetic.main.item_info_detail_feed.view.*


class DetailAdapter(private val myId: String) :
    RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    private val items = mutableListOf<FeedImage>()
    var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val binding = DataBindingUtil.inflate<ItemInfoDetailFeedBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_info_detail_feed,
            parent,
            false
        )
        val viewHolder = DetailViewHolder(binding, myId)

        viewHolder.itemView.cl_comment.setOnClickListener {
            items[viewHolder.adapterPosition].let {
                it.imageName?.let { imageName ->
                    onClickListener?.onClickComment(imageName)
                }
            }
        }
        viewHolder.itemView.ll_rating_evaluate_average.setOnClickListener {
            items[viewHolder.adapterPosition].let {
                onClickListener?.onClickGrade(it)
            }
        }

        viewHolder.itemView.iv_profile_evaluate_feed.setOnClickListener {
            items[viewHolder.adapterPosition].let {
                onClickListener?.onClickProfile(it)
            }
        }

        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size


    fun setFeedImages(list: List<FeedImage>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
        onClickListener?.setMyCurrentItem()
    }


    interface OnClickListener {
        fun onClickComment(imageName: String)
        fun onClickGrade(feedImage: FeedImage)
        fun onClickProfile(feedImage: FeedImage)
        fun setMyCurrentItem()
    }

    class DetailViewHolder(private val binding: ItemInfoDetailFeedBinding, private val myId: String) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bind(feedImage: FeedImage) {
            binding.myId = myId
            binding.feedImage = feedImage
            with(itemView) {
                Glide.with(context)
                    .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${feedImage.user?.id}/feed/${feedImage.imageName}")
                    .into(binding.ivItemEvaluateFeed)

                if (feedImage.user?.profileImage.isNullOrEmpty()) {
                    binding.ivProfileEvaluateFeed.setImageDrawable(context.getDrawable(R.drawable.ic_person_black))
                } else {
                    Glide.with(context)
                        .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${feedImage.user?.id}/profile/${feedImage.user?.profileImage}")
                        .error(context.getDrawable(R.drawable.ic_person_black))
                        .placeholder(context.getDrawable(R.drawable.ic_person_black))
                        .into(binding.ivProfileEvaluateFeed)
                }
            }
        }
    }


}
