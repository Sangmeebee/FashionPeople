package com.sangmee.fashionpeople.ui.fragment.search.brand.result

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sangmee.fashionpeople.ui.fragment.search.brand.result.content.SearchRecentBrandContentFragment
import com.sangmee.fashionpeople.ui.fragment.search.brand.result.content.SearchScoreBrandContentFragment

class ResultSearchBrandViewPagerAdapter(fragment: Fragment, query: String?) :
    FragmentStateAdapter(fragment) {

    private val searchScoreBrandContentFragment =
        SearchScoreBrandContentFragment.newInstance(query!!)
    private val searchRecentBrandContentFragment =
        SearchRecentBrandContentFragment.newInstance(query!!)

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> searchScoreBrandContentFragment
            else -> searchRecentBrandContentFragment
        }
    }
}
