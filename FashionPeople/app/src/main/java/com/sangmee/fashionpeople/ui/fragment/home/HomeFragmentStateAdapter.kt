package com.sangmee.fashionpeople.ui.fragment.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sangmee.fashionpeople.ui.fragment.home.evaluate.EvaluateFragment
import com.sangmee.fashionpeople.ui.fragment.home.following.FollowingFragment

class HomeFragmentStateAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> EvaluateFragment()
            else -> FollowingFragment()
        }

    override fun getItemCount(): Int = 2
}