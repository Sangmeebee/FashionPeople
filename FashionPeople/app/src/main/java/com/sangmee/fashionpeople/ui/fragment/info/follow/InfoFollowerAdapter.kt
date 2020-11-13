package com.sangmee.fashionpeople.ui.fragment.info.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.databinding.ItemFollowBinding

class InfoFollowerAdapter : RecyclerView.Adapter<InfoFollowerAdapter.InfoFollowViewHolder>() {

    private val followList = arrayListOf<FUser>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoFollowViewHolder {
        val binding = DataBindingUtil.inflate<ItemFollowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_follow,
            parent,
            false
        )
        return InfoFollowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoFollowViewHolder, position: Int) {
        holder.bind(followList[position])
    }

    override fun getItemCount() = followList.size

    fun clearAndAddItems(followers: List<FUser>) {
        followList.clear()
        followList.addAll(followers)
        notifyDataSetChanged()
    }

    class InfoFollowViewHolder(private val binding: ItemFollowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isGone = true

        fun bind(follow: FUser) {
            binding.follow = follow
            follow.instagramId?.let {
                if (it.isNotEmpty()) {
                    isGone = false
                }
            }
            binding.isGone = isGone
            binding.executePendingBindings()
        }
    }
}
