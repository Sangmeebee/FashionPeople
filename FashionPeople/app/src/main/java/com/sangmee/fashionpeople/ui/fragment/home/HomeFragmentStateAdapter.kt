package com.sangmee.fashionpeople.ui.fragment.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sangmee.fashionpeople.ui.fragment.home.following.FollowingFragment
import com.sangmee.fashionpeople.ui.fragment.home.hot.HotFragment
import com.sangmee.fashionpeople.ui.fragment.home.newfrag.NewFragment

class HomeFragmentStateAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> NewFragment()
            1 -> HotFragment()
            else -> FollowingFragment()
        }

    override fun getItemCount(): Int = 3
}