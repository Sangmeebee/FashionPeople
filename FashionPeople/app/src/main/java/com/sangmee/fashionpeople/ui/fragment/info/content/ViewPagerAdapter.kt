package com.sangmee.fashionpeople.ui.fragment.info.content

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment, private val userId: String) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> FeedImageFragment(userId)
            else -> SavedImageFragment(userId)
        }
    }
}
