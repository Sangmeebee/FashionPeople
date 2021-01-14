package com.sangmee.fashionpeople.ui.fragment.search.style

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.ItemSearchStyleBinding

class SearchStyleAdapter(private val onStyleItemSelectedInterface: OnStyleItemSelectedInterface) :
    RecyclerView.Adapter<SearchStyleAdapter.SearchStyleViewHolder>() {

    private val styleList = mutableListOf<String>()
    private val numList = mutableListOf<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchStyleViewHolder {
        val binding = DataBindingUtil.inflate<ItemSearchStyleBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_search_style,
            parent,
            false
        )

        val result = SearchStyleViewHolder(binding)
        binding.root.setOnClickListener {
            val query = styleList[result.adapterPosition]
            onStyleItemSelectedInterface.onItemSelected(query)
        }
        return result
    }

    override fun onBindViewHolder(holder: SearchStyleViewHolder, position: Int) {
        holder.bind(styleList[position], numList[position])
    }

    override fun getItemCount() = styleList.size

    fun setStyleList(styleList: List<String>, numList: List<Int>) {
        with(this.styleList) {
            clear()
            addAll(styleList)
        }
        with(this.numList) {
            clear()
            addAll(numList)
        }
        notifyDataSetChanged()
    }

    class SearchStyleViewHolder(private val binding: ItemSearchStyleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(styleName: String, num: Int) {
            binding.tvStyleName.text = styleName
            binding.tvPostNum.text = "게시물 ${num}개"
            binding.executePendingBindings()
        }
    }
}
