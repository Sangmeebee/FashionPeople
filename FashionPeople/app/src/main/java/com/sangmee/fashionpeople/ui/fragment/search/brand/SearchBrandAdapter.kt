package com.sangmee.fashionpeople.ui.fragment.search.brand

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.Brand
import com.sangmee.fashionpeople.databinding.ItemSearchBrandBinding

class SearchBrandAdapter(private val onBrandItemSelectedInterface: OnBrandItemSelectedInterface) :
    RecyclerView.Adapter<SearchBrandAdapter.SearchBrandViewHolder>() {

    private val brandList = mutableListOf<Brand>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchBrandViewHolder {
        val binding = DataBindingUtil.inflate<ItemSearchBrandBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_search_brand,
            parent,
            false
        )

        val result = SearchBrandViewHolder(binding)
        binding.root.setOnClickListener {
            val query = brandList[result.adapterPosition]
            onBrandItemSelectedInterface.onItemSelected(query.brandName)
        }
        return result
    }

    override fun onBindViewHolder(holder: SearchBrandViewHolder, position: Int) {
        holder.bind(brandList[position])
    }

    override fun getItemCount() = brandList.size

    fun setBrandList(brandList: List<Brand>) {
        with(this.brandList) {
            clear()
            addAll(brandList)
        }
        notifyDataSetChanged()
    }

    class SearchBrandViewHolder(private val binding: ItemSearchBrandBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(brand: Brand) {
            binding.tvBrandName.text = brand.brandName
            binding.tvPostNum.text = "게시물 ${brand.postNum}개"
            binding.executePendingBindings()
        }
    }
}
