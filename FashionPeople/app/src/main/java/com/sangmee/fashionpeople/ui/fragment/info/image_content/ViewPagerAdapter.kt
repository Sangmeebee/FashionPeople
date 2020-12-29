package com.sangmee.fashionpeople.ui.fragment.info.image_content

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment, private val userId: String) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> FeedImageFragment.newInstance(userId)
            else -> SavedImageFragment(userId)
        }
    }
}
