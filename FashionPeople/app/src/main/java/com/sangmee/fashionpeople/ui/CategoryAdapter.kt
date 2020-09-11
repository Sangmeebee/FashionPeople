package com.sangmee.fashionpeople.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R

class CategoryAdapter :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val titleList = arrayListOf("카테고리", "상의", "하의", "신발", "악세서리")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_category, parent,
            false
        )
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.bind(titleList[position])
    }

    override fun getItemCount() = titleList.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryTitle = itemView.findViewById<TextView>(R.id.tv_category_title)

        fun bind(title: String) {
            categoryTitle.text = title
        }

    }
}
