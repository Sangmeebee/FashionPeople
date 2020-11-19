package com.sangmee.fashionpeople.ui.fragment.info.other

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sangmee.fashionpeople.ui.fragment.info.content.FeedImageFragment
import com.sangmee.fashionpeople.ui.fragment.info.content.SavedImageFragment

class OtherViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> FeedImageFragment()
            else -> SavedImageFragment()
        }
    }
}
