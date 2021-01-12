package com.sangmee.fashionpeople.ui.fragment.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.ui.fragment.info.image_content.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_info.*


class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //tablayout 세팅
        setTabLayout()
    }

    private fun setTabLayout() {
        viewPager.adapter = SearchViewPagerAdapter(this)

        TabLayoutMediator(tl_container, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "브랜드"
                }
                else -> {
                    tab.text = "친구"
                }
            }
        }.attach()
    }
}
