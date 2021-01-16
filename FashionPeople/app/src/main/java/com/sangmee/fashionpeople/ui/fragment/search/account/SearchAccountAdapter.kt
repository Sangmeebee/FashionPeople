package com.sangmee.fashionpeople.ui.fragment.search.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.databinding.ItemSearchAccountBinding

class SearchAccountAdapter(private val onAccountItemSelectedInterface: OnAccountItemSelectedInterface) :
    RecyclerView.Adapter<SearchAccountAdapter.SearchAccountViewHolder>() {

    private val searchUserList = mutableListOf<FUser>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAccountViewHolder {
        val binding = DataBindingUtil.inflate<ItemSearchAccountBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_search_account,
            parent,
            false
        )

        val result = SearchAccountViewHolder(binding)
        binding.root.setOnClickListener {
            val user = searchUserList[result.adapterPosition]
            onAccountItemSelectedInterface.onItemSelected(user)

        }
        return result
    }

    override fun onBindViewHolder(holder: SearchAccountViewHolder, position: Int) {
        holder.bind(searchUserList[position])
    }

    override fun getItemCount() = searchUserList.size

    fun setAccountList(searchUserList: List<FUser>) {
        with(this.searchUserList) {
            clear()
            addAll(searchUserList)
        }
        notifyDataSetChanged()
    }

    class SearchAccountViewHolder(private val binding: ItemSearchAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: FUser) {
            binding.user = user
            binding.isGone = user.introduce.isNullOrEmpty()
            binding.executePendingBindings()
        }
    }
}
