package com.sangmee.fashionpeople.ui.fragment.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.ItemCommentBinding
import com.sangmee.fashionpeople.databinding.ItemHomeFeedBinding
import com.sangmee.fashionpeople.ui.fragment.home.HomeFeedViewHolder
import kotlinx.android.synthetic.main.item_home_feed.view.*

class CommentRecyclerAdapter : RecyclerView.Adapter<CommentRecyclerViewHolder>() {

    private val items = mutableListOf<Comment>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentRecyclerViewHolder {
        val binding = DataBindingUtil.inflate<ItemCommentBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_comment,
            parent,
            false
        )
        val viewHolder = CommentRecyclerViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: CommentRecyclerViewHolder, position: Int) {
        return holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setComments(list: List<Comment>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

}