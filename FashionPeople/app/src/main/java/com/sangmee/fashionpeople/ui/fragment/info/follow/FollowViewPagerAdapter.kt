package com.sangmee.fashionpeople.ui.fragment.info.follow

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FollowViewPagerAdapter(fragment: Fragment, private val userId: String) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> InfoFollowerFragment(userId)
            else -> InfoFollowingFragment(userId)
        }
    }
}
