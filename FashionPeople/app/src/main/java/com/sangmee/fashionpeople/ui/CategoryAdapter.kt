package com.sangmee.fashionpeople.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.kakaologin.GlobalApplication

class CategoryAdapter(private val onListItemSelectedInterface: OnListItemSelectedInterface) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val titleList = arrayListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_category, parent,
            false
        )

        val result = CategoryViewHolder(view)

        view.setOnClickListener {
            val title = titleList[result.adapterPosition]
            onListItemSelectedInterface.onItemSelected(title)
        }
        return result
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.bind(titleList[position])
    }

    override fun getItemCount() = titleList.size

    fun setTitleList(titleList: ArrayList<String>) {
        with(this.titleList) {
            clear()
            addAll(titleList)
        }
        notifyDataSetChanged()
    }
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val brandName = itemView.findViewById<TextView>(R.id.tv_brand_name)

        fun bind(title: String) {
            brandName.text = title
        }

    }
}
