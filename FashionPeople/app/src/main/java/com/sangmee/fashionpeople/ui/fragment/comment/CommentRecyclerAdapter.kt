package com.sangmee.fashionpeople.ui.fragment.comment

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.databinding.ItemCommentBinding
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentRecyclerAdapter(
    private val onClickListener: OnClickListener,
    private val customId: String
) :
    RecyclerView.Adapter<CommentRecyclerAdapter.CommentRecyclerViewHolder>() {

    private val items = mutableListOf<Comment>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentRecyclerViewHolder {
        val binding = DataBindingUtil.inflate<ItemCommentBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_comment,
            parent,
            false
        )
        val viewHolder = CommentRecyclerViewHolder(binding)

        viewHolder.itemView.cl_container.setOnLongClickListener {
            items[viewHolder.adapterPosition].let {
                if (it.user?.id == customId || it.feedImage?.user?.id == customId) {
                    it.id?.let { id ->
                        onClickListener.longClick(id)
                    }
                }
            }
            false
        }

        viewHolder.itemView.iv_profile_image.setOnClickListener {
            items[viewHolder.adapterPosition].let {
                it.user?.let { user ->
                    onClickListener.clickProfile(user.id!!)
                }
            }
        }
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

    interface OnClickListener {
        fun longClick(id: Int)
        fun clickProfile(id: String)
    }


    class CommentRecyclerViewHolder(
        private val binding: ItemCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bind(item: Comment) {
            with(binding) {
                comment = item

                if (item.user?.profileImage.isNullOrEmpty()) {
                    binding.ivProfileImage.setImageDrawable(binding.root.context.getDrawable(R.drawable.ic_user))
                } else {
                    Glide.with(itemView.context)
                        .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${item.user?.id}/profile/${item.user?.profileImage}")
                        .error(itemView.context.getDrawable(R.drawable.ic_user))
                        .placeholder(itemView.context.getDrawable(R.drawable.ic_user))
                        .into(binding.ivProfileImage)
                    executePendingBindings()
                }
            }
        }
    }
}
