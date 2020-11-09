package com.sangmee.fashionpeople.ui.fragment.home

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.ItemHomeFeedBinding
import com.sangmee.fashionpeople.retrofit.model.FeedImage
import com.sangmee.fashionpeople.ui.fragment.home.evaluate.EvaluateViewModel
import kotlinx.android.synthetic.main.item_home_feed.view.*

class HomeFeedAdapter(private val myId: String) : RecyclerView.Adapter<HomeFeedViewHolder>() {

    private val items = mutableListOf<FeedImage>()
    var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFeedViewHolder {
        val binding = DataBindingUtil.inflate<ItemHomeFeedBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_home_feed,
            parent,
            false
        )
        val viewHolder = HomeFeedViewHolder(binding, myId)
        viewHolder.itemView.rb_home_feed.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            items[viewHolder.adapterPosition].let {
                onClickListener?.onClickRatingBar(ratingBar, rating, fromUser, it)
            }
        }

        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: HomeFeedViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size


    fun setFeedImages(list: List<FeedImage>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun removeImage(feedImage: FeedImage) {
        for (i in items.indices) {
            if (items[i].imageName == feedImage.imageName) {
                items.remove(items[i])
                notifyItemRemoved(i)
            }
        }
    }

    fun updateItem(feedImage: FeedImage) {
        for (index in items.indices) {
            if (items[index].imageName == feedImage.imageName) {
                items[index] = feedImage
                notifyItemChanged(index)
                Log.d("seunghwan", "aaa")
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
    }

}