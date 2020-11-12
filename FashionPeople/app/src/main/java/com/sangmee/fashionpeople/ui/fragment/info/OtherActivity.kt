package com.sangmee.fashionpeople.ui.fragment.info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.ui.fragment.info.content.OtherViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_info.*

class OtherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)

        //tablayout 세팅
        viewPager.adapter = OtherViewPagerAdapter(this)

        TabLayoutMediator(tl_container, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "게시물"
                else -> tab.text = "저장함"
            }
        }.attach()
    }
}
