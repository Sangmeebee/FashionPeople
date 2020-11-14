package com.sangmee.fashionpeople.ui.fragment.info.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.databinding.ItemFollowerBinding
import com.sangmee.fashionpeople.databinding.ItemFollowingBinding

class InfoFollowingAdapter : RecyclerView.Adapter<InfoFollowingAdapter.InfoFollowingViewHolder>() {

    private val followList = arrayListOf<FUser>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoFollowingViewHolder {
        val binding = DataBindingUtil.inflate<ItemFollowingBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_following,
            parent,
            false
        )
        return InfoFollowingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoFollowingViewHolder, position: Int) {
        holder.bind(followList[position])
    }

    override fun getItemCount() = followList.size

    fun clearAndAddItems(followings: List<FUser>) {
        followList.clear()
        followList.addAll(followings)
        notifyDataSetChanged()
    }

    class InfoFollowingViewHolder(private val binding: ItemFollowingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isGone = true

        fun bind(following: FUser) {
            binding.following = following
            following.instagramId?.let {
                if (it.isNotEmpty()) {
                    isGone = false
                }
            }
            binding.isGone = isGone
            binding.executePendingBindings()
        }
    }
}

