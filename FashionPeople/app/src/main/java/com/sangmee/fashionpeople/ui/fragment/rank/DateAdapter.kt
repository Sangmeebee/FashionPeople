package com.sangmee.fashionpeople.ui.fragment.rank

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.marginStart
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.CustomDate
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.ItemDateBinding
import java.lang.Math.abs

class DateAdapter : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    private val items = mutableListOf<CustomDate>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding = DataBindingUtil.inflate<ItemDateBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_date,
            parent,
            false
        )

        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setCustomDates(list: List<CustomDate>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class DateViewHolder(
        val binding: ItemDateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(customDate: CustomDate) {
            binding.tvDate.text = "#${customDate.date}"
            val rankRecyclerAdapter = RankRecyclerAdapter()
            rankRecyclerAdapter.setRankImages(customDate.rankImages)

            val nextItemVisiblePx = itemView.resources.getDimension(R.dimen.viewpager_next_item_visible)
            val currentItemHorizontalMarginPx = itemView.resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
            val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
            binding.vpRanking.apply {
                adapter = rankRecyclerAdapter
                offscreenPageLimit = 1
                setPageTransformer { page, position ->
                    page.translationX = -pageTranslationX * position
                    page.scaleY = 1 - (0.25f * abs(position))
                }
                addItemDecoration(HorizontalMarginItemDecoration(itemView.context, R.dimen.viewpager_current_item_horizontal_margin))
            }

        }


    }
}