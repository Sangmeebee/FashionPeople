package com.sangmee.fashionpeople.ui.add

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R

class CategoryAdapter(private val onListItemSelectedInterface: OnListItemSelectedInterface) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val titleList = arrayListOf<String>()
    private val numList = arrayListOf<Int>()

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

        holder.bind(titleList[position], numList[position])
    }

    override fun getItemCount() = titleList.size

    fun setTitleList(titleList: List<String>, numList: List<Int>) {
        with(this.titleList) {
            clear()
            addAll(titleList)
        }
        with(this.numList) {
            clear()
            addAll(numList)
        }
        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val brandName = itemView.findViewById<TextView>(R.id.tv_brand_name)
        private val postNum = itemView.findViewById<TextView>(R.id.tv_post_num)

        fun bind(title: String, num: Int) {
            brandName.text = title
            postNum.text = "게시물 ${num}개"
        }

    }
}
