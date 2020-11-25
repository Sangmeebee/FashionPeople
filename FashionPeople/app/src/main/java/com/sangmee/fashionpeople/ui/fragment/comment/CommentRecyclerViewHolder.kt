package com.sangmee.fashionpeople.ui.fragment.comment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.databinding.ItemCommentBinding

class CommentRecyclerViewHolder(
    private val binding: ItemCommentBinding
) : RecyclerView.ViewHolder(binding.root) {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun bind(item: Comment) {
        Log.d("seunghwan", "binditem = $item")
        with(binding) {
            comment = item
            Glide.with(itemView.context)
                .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${item.user?.id}/profile/${item.user?.profileImage}")
                .error(itemView.context.getDrawable(R.drawable.ic_person_white))
                .placeholder(itemView.context.getDrawable(R.drawable.ic_person_white))
                .into(binding.ivProfileImage)
            executePendingBindings()
        }
    }
}