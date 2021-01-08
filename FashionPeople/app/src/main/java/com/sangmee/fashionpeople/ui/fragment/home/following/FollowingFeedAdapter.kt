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
import kotlinx.android.synthetic.main.item_evaluate_feed.view.*
import kotlinx.android.synthetic.main.item_following_feed.view.*
import kotlinx.android.synthetic.main.item_following_feed.view.iv_delete_image
import kotlinx.android.synthetic.main.item_following_feed.view.iv_save_image
import kotlinx.android.synthetic.main.item_following_feed.view.ll_container
import kotlinx.android.synthetic.main.item_following_feed.view.tv_comment

class FollowingFeedAdapter(private val myId: String, private val feedIsSaved: (String) -> Unit, private val feedIsDeleted: (String) -> Unit) :
    RecyclerView.Adapter<FollowingFeedViewHolder>() {

    private val items = mutableListOf<FeedImage>()
    var onClickListener: FollowingFeedAdapter.OnClickListener? = null
    private val isSaved = mutableMapOf<String, Boolean>()

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
        viewHolder.itemView.tv_comment.setOnClickListener {
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
        viewHolder.itemView.ll_container.setOnClickListener {
            items[viewHolder.adapterPosition].let {
                onClickListener?.onClickProfile(it)
            }
        }


        viewHolder.itemView.iv_save_image.setOnClickListener {
            items[viewHolder.adapterPosition].let{
                onClickListener?.onClickSave(it.imageName!!)
                feedIsSaved(it.imageName!!)
            }
        }

        viewHolder.itemView.iv_delete_image.setOnClickListener {
            items[viewHolder.adapterPosition].let{
                onClickListener?.onClickDelete(it.imageName!!)
                feedIsDeleted(it.imageName!!)
            }
        }

        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: FollowingFeedViewHolder, position: Int) {
        isSaved[items[position].imageName]?.let{
            holder.bind(items[position], it)
        }
    }

    override fun getItemCount(): Int = items.size

    fun setFeedImages(list: List<FeedImage>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun setSavedButtonType(isSave: Map<String, Boolean>) {
        isSaved.clear()
        isSaved.putAll(isSave)
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

        fun onClickSave(imageName: String)
        fun onClickDelete(imageName: String)
        fun onClickComment(imageName: String)
        fun onClickGrade(feedImage: FeedImage)
        fun onClickProfile(feedImage: FeedImage)
    }
}
