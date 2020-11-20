package com.sangmee.fashionpeople.ui.fragment.comment

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.databinding.ItemCommentBinding

class CommentRecyclerViewHolder(
    private val binding: ItemCommentBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Comment) {
        Log.d("seunghwan", "binditem = $item")
        with(binding) {
            comment = item
            executePendingBindings()
        }
    }
}