package com.sangmee.fashionpeople.ui.fragment.info.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.databinding.ItemFollowerBinding

class InfoFollowerAdapter : RecyclerView.Adapter<InfoFollowerAdapter.InfoFollowerViewHolder>() {

    private val followList = arrayListOf<FUser>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoFollowerViewHolder {
        val binding = DataBindingUtil.inflate<ItemFollowerBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_follower,
            parent,
            false
        )
        return InfoFollowerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoFollowerViewHolder, position: Int) {
        holder.bind(followList[position])
    }

    override fun getItemCount() = followList.size

    fun clearAndAddItems(followers: List<FUser>) {
        followList.clear()
        followList.addAll(followers)
        notifyDataSetChanged()
    }

    class InfoFollowerViewHolder(private val binding: ItemFollowerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isGone = true

        fun bind(follower: FUser) {
            binding.follower = follower
            follower.instagramId?.let {
                if (it.isNotEmpty()) {
                    isGone = false
                }
            }
            binding.isGone = isGone
            binding.executePendingBindings()
        }
    }
}
