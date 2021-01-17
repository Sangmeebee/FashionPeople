package com.sangmee.fashionpeople.ui.fragment.search.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.databinding.ItemRecentSearchAccountBinding

class RecentSearchAccountAdapter(private val onAccountItemSelectedInterface: OnAccountItemSelectedInterface) :
    RecyclerView.Adapter<RecentSearchAccountAdapter.SearchAccountViewHolder>() {

    private val searchUserList = mutableListOf<FUser?>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAccountViewHolder {
        val binding = DataBindingUtil.inflate<ItemRecentSearchAccountBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_recent_search_account,
            parent,
            false
        )
        val result = SearchAccountViewHolder(binding)

        binding.root.setOnClickListener {
            val user = searchUserList[result.adapterPosition]
            user?.let { onAccountItemSelectedInterface.onItemSelected(user) }

        }
        binding.flCancel.setOnClickListener {
            val user = searchUserList[result.adapterPosition]
            user?.let { onAccountItemSelectedInterface.onClickCancelBtn(user) }
        }

        return result
    }

    override fun onBindViewHolder(holder: SearchAccountViewHolder, position: Int) {
        searchUserList[position]?.let { holder.bind(it) }
    }

    override fun getItemCount() = searchUserList.size

    fun setAccountList(searchUserList: List<FUser>) {
        with(this.searchUserList) {
            clear()
            addAll(searchUserList)
        }
        notifyDataSetChanged()
    }

    class SearchAccountViewHolder(private val binding: ItemRecentSearchAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: FUser) {
            binding.user = user
            binding.isGone = user.introduce.isNullOrEmpty()
            binding.executePendingBindings()
        }
    }
}
