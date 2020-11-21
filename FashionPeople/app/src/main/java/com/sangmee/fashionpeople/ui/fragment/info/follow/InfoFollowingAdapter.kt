package com.sangmee.fashionpeople.ui.fragment.info.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.databinding.ItemFollowingBinding

class InfoFollowingAdapter(
    val setBtn: (String) -> Unit,
    val callActivity: (String, Boolean) -> Unit
) :
    RecyclerView.Adapter<InfoFollowingAdapter.InfoFollowingViewHolder>() {

    private val followList = arrayListOf<FUser>()
    private val isFollowings = mutableMapOf<String, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoFollowingViewHolder {
        val binding = DataBindingUtil.inflate<ItemFollowingBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_following,
            parent,
            false
        )
        binding.adapter = this
        return InfoFollowingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoFollowingViewHolder, position: Int) {
        holder.bind(followList[position], isFollowings[followList[position].id]!!)
    }

    override fun getItemCount() = followList.size

    fun clearAndAddItems(followings: List<FUser>) {
        followList.clear()
        followList.addAll(followings)
        notifyDataSetChanged()
    }

    fun clearAndAddButtonType(isFollowing: Map<String, Boolean>) {
        isFollowings.clear()
        isFollowings.putAll(isFollowing)
        notifyDataSetChanged()
    }

    fun setButton(id: String) {
        setBtn(id)
    }

    fun callOtherActivity(customId: String) {
        callActivity(customId, false)
    }

    class InfoFollowingViewHolder(private val binding: ItemFollowingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val customId = GlobalApplication.prefs.getString("custom_id", "empty")

        fun bind(following: FUser, isFollowing: Boolean) {
            var isGone = true
            var isMe = false

            binding.following = following
            following.instagramId?.let {
                if (it.isNotEmpty()) {
                    isGone = false
                }
            }
            if (following.id == customId) {
                isMe = true
            }
            binding.isMe = isMe
            binding.id = following.id
            binding.isGone = isGone
            binding.isFollowing = isFollowing
            binding.executePendingBindings()
        }
    }
}

