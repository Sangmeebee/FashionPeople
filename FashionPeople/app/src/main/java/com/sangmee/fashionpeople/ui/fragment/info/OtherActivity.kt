package com.sangmee.fashionpeople.ui.fragment.info

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.ActivityOtherBinding
import com.sangmee.fashionpeople.observer.OtherViewModel
import com.sangmee.fashionpeople.ui.fragment.info.content.OtherViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_info.*

class OtherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtherBinding
    private val otherVm by viewModels<OtherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other)
        binding.apply {
            val customId = intent.getStringExtra("customId")
            val isFollowing = intent.getBooleanExtra("buttonState", false)
            otherVm.callProfile(customId)
            vm = otherVm
            this.isFollowing = isFollowing
            this.customId = customId
            lifecycleOwner = this@OtherActivity
        }
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
