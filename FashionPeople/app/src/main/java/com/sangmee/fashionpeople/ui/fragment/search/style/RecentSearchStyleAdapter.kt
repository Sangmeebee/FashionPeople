package com.sangmee.fashionpeople.ui.fragment.search.style

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import kotlinx.android.synthetic.main.item_recent_search_brand.view.*

class RecentSearchStyleAdapter(private val onStyleItemSelectedInterface: OnStyleItemSelectedInterface) :
    RecyclerView.Adapter<RecentSearchStyleAdapter.SearchStyleViewHolder>() {

    private val styleList = mutableListOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchStyleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_search_brand, parent, false)
        val viewHolder = SearchStyleViewHolder(view)

        viewHolder.itemView.setOnClickListener {
            val query = styleList[viewHolder.adapterPosition]
            onStyleItemSelectedInterface.onItemSelected(query)
        }
        viewHolder.itemView.fl_cancel.setOnClickListener {
            val query = styleList[viewHolder.adapterPosition]
            onStyleItemSelectedInterface.onClickCancelBtn(query)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: SearchStyleViewHolder, position: Int) {
        holder.bind(styleList[position])
    }

    override fun getItemCount() = styleList.size

    fun setStyleList(styleList: List<String>) {
        with(this.styleList) {
            clear()
            addAll(styleList)
        }
        notifyDataSetChanged()
    }

    class SearchStyleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(styleName: String) {
            itemView.tv_brand_name.text = "#$styleName"
        }
    }
}
