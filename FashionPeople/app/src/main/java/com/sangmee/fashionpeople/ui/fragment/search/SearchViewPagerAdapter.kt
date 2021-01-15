package com.sangmee.fashionpeople.ui.fragment.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sangmee.fashionpeople.ui.fragment.search.brand.SearchBrandFragment
import com.sangmee.fashionpeople.ui.fragment.search.style.SearchStyleFragment

class SearchViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val searchStyleFragment = SearchStyleFragment()
    private val searchBrandFragment = SearchBrandFragment()
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> searchStyleFragment
            1 -> searchBrandFragment
            else -> SearchStyleFragment()

        }
    }
}
