package com.sangmee.fashionpeople.ui.fragment.info.follow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.ActivityFollowBinding
import kotlinx.android.synthetic.main.activity_follow.*


class FollowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFollowBinding
    private var fragmentId = 0
    private var userName = "닉네임"
    private var followerNum = 0
    private var followingNum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_follow)

        val bundle = intent.extras
        bundle?.let {
            fragmentId = it.getInt("FRAGMENT_ID", 0)
            userName = it.getString("USER_NAME", "닉네임")
            followerNum = it.getInt("FOLLOWER_NUM", 0)
            followingNum = it.getInt("FOLLOWING_NUM", 0)
        }

        setToolbar(binding.tbFollow)
            binding.tvName.text = userName
            setTabLayout()
    }

    private fun setTabLayout() {
        //tablayout 세팅
        vp_follow.adapter = FollowViewPagerAdapter(this)
        vp_follow.currentItem = fragmentId
        TabLayoutMediator(tl_container, vp_follow) { tab, position ->
            when (position) {
                0 -> tab.text =
                    "팔로워 ${followerNum}명" //TODO vm를 MainActivity에서 가져서 vm.followerNum 불러오기
                else -> tab.text = "팔로잉 ${followingNum}명"
            }
        }.attach()
    }

    private fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow)
            setDisplayShowTitleEnabled(false)
        }
    }
}
