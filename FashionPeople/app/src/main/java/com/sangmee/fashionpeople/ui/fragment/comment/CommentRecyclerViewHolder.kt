package com.sangmee.fashionpeople.ui.fragment.comment

import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.databinding.ItemCommentBinding

class CommentRecyclerViewHolder(
    private val binding: ItemCommentBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Comment) {
        with(binding) {
            comment = item
            executePendingBindings()
        }
    }
}