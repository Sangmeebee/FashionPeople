package com.sangmee.fashionpeople.ui.fragment.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.ItemTagBinding

class TagRecyclerViewAdapter : RecyclerView.Adapter<TagViewHolder>() {

    private val items = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val binding = DataBindingUtil.inflate<ItemTagBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_tag,
            parent,
            false
        )
        val viewHolder = TagViewHolder(binding)

        return viewHolder
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addTag(tag: String) {
        items.add(items.size, tag)
    }
}