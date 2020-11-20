package com.sangmee.fashionpeople.ui.fragment.home

import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.databinding.ItemTagBinding

class TagViewHolder(
    private val binding: ItemTagBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(item: String) {
        binding.apply {
            tag = item
            executePendingBindings()
        }
    }

}