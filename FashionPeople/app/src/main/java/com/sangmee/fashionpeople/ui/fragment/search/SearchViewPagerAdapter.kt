package com.sangmee.fashionpeople.ui.fragment.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SearchViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> SearchBrandFragment()
            else -> SearchBrandFragment()
        }
    }
}
