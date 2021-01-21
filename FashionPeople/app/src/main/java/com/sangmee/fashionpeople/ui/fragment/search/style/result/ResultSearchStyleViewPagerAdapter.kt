package com.sangmee.fashionpeople.ui.fragment.search.style.result

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sangmee.fashionpeople.ui.fragment.search.style.result.content.SearchRecentStyleContentFragment
import com.sangmee.fashionpeople.ui.fragment.search.style.result.content.SearchScoreStyleContentFragment

class ResultSearchStyleViewPagerAdapter(fragment: Fragment, query: String?) :
    FragmentStateAdapter(fragment) {

    private val searchScoreStyleContentFragment =
        SearchScoreStyleContentFragment.newInstance(query!!)
    private val searchRecentStyleContentFragment =
        SearchRecentStyleContentFragment.newInstance(query!!)

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> searchScoreStyleContentFragment
            else -> searchRecentStyleContentFragment
        }
    }
}
