package com.sangmee.fashionpeople.ui.fragment.rank

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.RankImage
import com.sangmee.fashionpeople.databinding.ItemRankContentBinding

class RankRecyclerAdapter : RecyclerView.Adapter<RankContentViewHolder>() {

    private val items = mutableListOf<RankImage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankContentViewHolder {
        val binding = DataBindingUtil.inflate<ItemRankContentBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_rank_content,
            parent,
            false
        )

        binding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        return RankContentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankContentViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    fun setRankImages(list: List<RankImage>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}
