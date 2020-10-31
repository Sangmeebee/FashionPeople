package com.sangmee.fashionpeople.ui.fragment.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.ItemHomeFeedBinding
import com.sangmee.fashionpeople.retrofit.model.FeedImage
import kotlinx.android.synthetic.main.item_home_feed.view.*

class HomeFeedAdapter: RecyclerView.Adapter<HomeFeedViewHolder>() {

    private val items = mutableListOf<FeedImage>()
    var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFeedViewHolder {
        val binding = DataBindingUtil.inflate<ItemHomeFeedBinding>(LayoutInflater.from(parent.context), R.layout.item_home_feed, parent, false)
        val viewHolder = HomeFeedViewHolder(binding)
        viewHolder.itemView.rb_home_feed.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            onClickListener?.onClickRatingBar(
                ratingBar,
                rating,
                fromUser
            )
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: HomeFeedViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size


    fun setFeedImages(list: List<FeedImage>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onClickRatingBar(ratingBar: RatingBar?, rating: Float, fromUser: Boolean)
    }

}