package com.sangmee.fashionpeople.ui.fragment.rank.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.CustomDate
import com.sangmee.fashionpeople.databinding.ItemDateBinding

class ManRankAdapter : RecyclerView.Adapter<ManRankAdapter.ManRankViewHolder>() {
    private val items = mutableListOf<CustomDate>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ManRankViewHolder {
        val binding = DataBindingUtil.inflate<ItemDateBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_date,
            parent,
            false
        )

        return ManRankViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ManRankViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setCustomDates(list: List<CustomDate>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class ManRankViewHolder(private val binding: ItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(customDate: CustomDate) {
            binding.tvDate.text = "${customDate.date}"
            val rankRecyclerAdapter = RankRecyclerAdapter()
            rankRecyclerAdapter.setRankImages(customDate.rankImages)

            binding.rvRanking.apply {
                adapter = rankRecyclerAdapter
            }
        }
    }
}
