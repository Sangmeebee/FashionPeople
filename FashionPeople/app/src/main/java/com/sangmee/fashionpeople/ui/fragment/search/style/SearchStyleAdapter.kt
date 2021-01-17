package com.sangmee.fashionpeople.ui.fragment.search.style

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.Style
import com.sangmee.fashionpeople.databinding.ItemSearchStyleBinding

class SearchStyleAdapter(private val onStyleItemSelectedInterface: OnStyleItemSelectedInterface) :
    RecyclerView.Adapter<SearchStyleAdapter.SearchStyleViewHolder>() {

    private val styleList = mutableListOf<Style>()
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
            onStyleItemSelectedInterface.onItemSelected(query.styleName)
        }
        return result
    }

    override fun onBindViewHolder(holder: SearchStyleViewHolder, position: Int) {
        holder.bind(styleList[position])
    }

    override fun getItemCount() = styleList.size

    fun setStyleList(styleList: List<Style>) {
        with(this.styleList) {
            clear()
            addAll(styleList)
        }
        notifyDataSetChanged()
    }

    class SearchStyleViewHolder(private val binding: ItemSearchStyleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(style: Style) {
            binding.tvStyleName.text = style.styleName
            binding.tvPostNum.text = "게시물 ${style.postNum}개"
            binding.executePendingBindings()
        }
    }
}
