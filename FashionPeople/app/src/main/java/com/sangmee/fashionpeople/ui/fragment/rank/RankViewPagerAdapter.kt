package com.sangmee.fashionpeople.ui.fragment.rank

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sangmee.fashionpeople.ui.fragment.rank.content.ManRankFragment
import com.sangmee.fashionpeople.ui.fragment.rank.content.WomanRankFragment

class RankViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> ManRankFragment()
            else -> WomanRankFragment()
        }
    }
}
