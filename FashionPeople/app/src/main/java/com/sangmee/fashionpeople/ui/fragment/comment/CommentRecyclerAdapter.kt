package com.sangmee.fashionpeople.ui.fragment.comment

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.databinding.ItemCommentBinding

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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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