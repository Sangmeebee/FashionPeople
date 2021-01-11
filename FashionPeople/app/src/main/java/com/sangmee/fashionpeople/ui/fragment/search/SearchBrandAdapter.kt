package com.sangmee.fashionpeople.ui.fragment.search

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.ItemSearchBrandBinding

class SearchBrandAdapter : RecyclerView.Adapter<SearchBrandAdapter.SearchBrandViewHolder>() {

    private val brandList = mutableListOf<FeedImage>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchBrandViewHolder {
        val binding = DataBindingUtil.inflate<ItemSearchBrandBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_search_brand,
            parent,
            false
        )
        binding.adapter = this
        return SearchBrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchBrandViewHolder, position: Int) {
        holder.bind(brandList[position])
    }

    override fun getItemCount() = brandList.size

    fun setBrandList(brands: List<FeedImage>) {
        brandList.clear()
        brandList.addAll(brands)
        notifyDataSetChanged()
    }

    fun callBrandFragment() {
        Log.d("Sangmeebee", "fragment 이동")
    }

    class SearchBrandViewHolder(private val binding: ItemSearchBrandBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(feedImage: FeedImage) {

            binding.brandFeedImage = feedImage
            binding.executePendingBindings()
        }
    }
}
