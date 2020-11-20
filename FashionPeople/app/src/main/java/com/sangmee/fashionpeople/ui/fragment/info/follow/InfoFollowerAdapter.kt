package com.sangmee.fashionpeople.ui.fragment.info.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.databinding.ItemFollowerBinding

class InfoFollowerAdapter(val setBtn: (String) -> Unit, val callActivity: (String) -> Unit) :
    RecyclerView.Adapter<InfoFollowerAdapter.InfoFollowerViewHolder>() {

    private val followList = arrayListOf<FUser>()
    private val isFollowings = mutableMapOf<String, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoFollowerViewHolder {
        val binding = DataBindingUtil.inflate<ItemFollowerBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_follower,
            parent,
            false
        )
        binding.adapter = this
        return InfoFollowerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoFollowerViewHolder, position: Int) {
        holder.bind(followList[position], isFollowings[followList[position].id]!!)
    }

    override fun getItemCount() = followList.size

    fun clearAndAddItems(followers: List<FUser>) {
        followList.clear()
        followList.addAll(followers)
        notifyDataSetChanged()
    }

    fun clearAndAddButtonType(isFollowing: Map<String, Boolean>) {
        isFollowings.clear()
        isFollowings.putAll(isFollowing)
        notifyDataSetChanged()
    }

    fun setButton(id: String){
        setBtn(id)
    }

    fun callOtherActivity(customId: String){
        callActivity(customId)
    }

    class InfoFollowerViewHolder(private val binding: ItemFollowerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(follower: FUser, isFollowing: Boolean) {
            var isGone = true

            binding.follower = follower
            follower.instagramId?.let {
                if (it.isNotEmpty()) {
                    isGone = false
                }
            }
            binding.id = follower.id
            binding.isGone = isGone
            binding.isFollowing = isFollowing
            binding.executePendingBindings()
        }
    }
}
