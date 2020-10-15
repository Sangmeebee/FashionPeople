package com.sangmee.fashionpeople.ui.fragment.content

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> PostImageFragment()
            else -> SavedImageFragment()
        }
    }
}
