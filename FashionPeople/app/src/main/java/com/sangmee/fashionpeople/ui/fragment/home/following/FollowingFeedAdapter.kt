package com.sangmee.fashionpeople.ui.fragment.home.following

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.ItemFollowingFeedBinding
import com.sangmee.fashionpeople.ui.fragment.home.evaluate.EvaluateFeedAdapter
import kotlinx.android.synthetic.main.item_evaluate_feed.view.ll_comment
import kotlinx.android.synthetic.main.item_following_feed.view.*

class FollowingFeedAdapter(private val myId: String) :
    RecyclerView.Adapter<FollowingFeedViewHolder>() {

    private val items = mutableListOf<FeedImage>()
    var onClickListener: FollowingFeedAdapter.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingFeedViewHolder {
        val binding = DataBindingUtil.inflate<ItemFollowingFeedBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_following_feed,
            parent,
            false
        )
        val viewHolder = FollowingFeedViewHolder(binding, myId)

        viewHolder.itemView.rb_following_feed.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            items[viewHolder.adapterPosition].let {
                onClickListener?.onClickRatingBar(ratingBar, rating, fromUser, it)
            }
        }
        viewHolder.itemView.ll_comment.setOnClickListener {
            items[viewHolder.adapterPosition].let {
                it.imageName?.let { imageName ->
                    onClickListener?.onClickComment(imageName)
                }
            }
        }
        viewHolder.itemView.ll_rating_average.setOnClickListener {
            items[viewHolder.adapterPosition].let {
                onClickListener?.onClickGrade(it)
            }
        }


        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: FollowingFeedViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setFeedImages(list: List<FeedImage>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun updateItem(feedImage: FeedImage) {
        for (index in items.indices) {
            if (items[index].imageName == feedImage.imageName) {
                items[index] = feedImage
                notifyItemChanged(index)
            }
        }
    }


    interface OnClickListener {
        fun onClickRatingBar(
            ratingBar: RatingBar?,
            rating: Float,
            fromUser: Boolean,
            feedImage: FeedImage
        )

        fun onClickComment(imageName: String)
        fun onClickGrade(feedImage: FeedImage)
    }
}