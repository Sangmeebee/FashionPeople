package com.sangmee.fashionpeople.ui.fragment.search.brand

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import kotlinx.android.synthetic.main.item_recent_search_brand.view.*
import kotlinx.android.synthetic.main.item_search_brand.view.*
import kotlinx.android.synthetic.main.item_search_brand.view.tv_brand_name

class RecentSearchBrandAdapter(private val onBrandItemSelectedInterface: OnBrandItemSelectedInterface) :
    RecyclerView.Adapter<RecentSearchBrandAdapter.SearchBrandViewHolder>() {

    private val brandList = mutableListOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchBrandViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_search_brand, parent, false)
        val viewHolder = SearchBrandViewHolder(view)

        viewHolder.itemView.setOnClickListener {
            val query = brandList[viewHolder.adapterPosition]
            onBrandItemSelectedInterface.onItemSelected(query)
        }
        viewHolder.itemView.fl_cancel.setOnClickListener {
            val query = brandList[viewHolder.adapterPosition]
            onBrandItemSelectedInterface.onClickCancelBtn(query)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: SearchBrandViewHolder, position: Int) {
        holder.bind(brandList[position])
    }

    override fun getItemCount() = brandList.size

    fun setBrandList(brandList: List<String>) {
        with(this.brandList) {
            clear()
            addAll(brandList)
        }
        notifyDataSetChanged()
    }

    class SearchBrandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(brandName: String) {
            itemView.tv_brand_name.text = "#$brandName"
        }
    }
}
