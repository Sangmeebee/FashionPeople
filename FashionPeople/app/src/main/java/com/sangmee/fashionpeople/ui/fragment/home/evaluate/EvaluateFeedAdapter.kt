package com.sangmee.fashionpeople.ui.fragment.home.evaluate

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.ItemEvaluateFeedBinding
import kotlinx.android.synthetic.main.item_evaluate_feed.view.*


class EvaluateFeedAdapter(private val myId: String) : RecyclerView.Adapter<EvaluateFeedViewHolder>() {

    private val items = mutableListOf<FeedImage>()
    var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvaluateFeedViewHolder {
        val binding = DataBindingUtil.inflate<ItemEvaluateFeedBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_evaluate_feed,
            parent,
            false
        )
        val viewHolder = EvaluateFeedViewHolder(binding, myId)

        viewHolder.itemView.rb_evaluate_feed.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
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
    override fun onBindViewHolder(holder: EvaluateFeedViewHolder, position: Int) {
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
        fun onClickProfile(feedImage: FeedImage)
    }


}
