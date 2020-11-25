package com.sangmee.fashionpeople.ui.fragment.info.other

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentOtherBinding
import com.sangmee.fashionpeople.observer.InfoViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.info.content.ViewPagerAdapter
import com.sangmee.fashionpeople.ui.fragment.info.follow.FollowFragment
import kotlinx.android.synthetic.main.fragment_info.*

private const val CUSTOM_ID = "param1"

class OtherFragment : Fragment() {
    private var customId: String? = null

    //otherfragment에서 내 계정 인지 판단
    var isMe = false

    lateinit var binding: FragmentOtherBinding
    private val infoVm: InfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            customId = it.getString(CUSTOM_ID)
            if (customId == infoVm.customId) {
                isMe = true
            }
            Log.d("FSP_CUSTOM_ID", customId.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        customId?.let {
            infoVm.callProfile(it)
            infoVm.callIsFollowing(it)
        }
        return inflater.inflate(R.layout.fragment_other, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.isMe = isMe
            binding.apply {
                customId = this@OtherFragment.customId
                otherVm = this@OtherFragment.infoVm
                lifecycleOwner = viewLifecycleOwner
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTabLayout()
        observeCallBack()
    }

    private fun setTabLayout() {
        viewPager.adapter = ViewPagerAdapter(this, customId!!)

        TabLayoutMediator(tl_container, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "게시물"
                else -> tab.text = "저장함"
            }
        }.attach()
    }

    private fun observeCallBack() {
        infoVm.followBtnEvent.observe(viewLifecycleOwner, Observer {
            btnForFollowing()
        })
        infoVm.callActivity.observe(viewLifecycleOwner, Observer {
            (activity as MainActivity).replaceFragmentUseBackStack(
                FollowFragment.newInstance(
                    it,
                    customId!!,
                    infoVm.userName.value!!,
                    infoVm.followerNum.value!!,
                    infoVm.followingNum.value!!
                )
            )
        })
    }

    private fun btnForFollowing() {
        customId?.let { customId ->
            infoVm.isFollowing.value?.let { isFollowing ->
                if (isFollowing) {
                    infoVm.deleteFollowing(customId)
                    infoVm.followerNum.value = infoVm.followerNum.value!! - 1
                    infoVm.isFollowing.value = !isFollowing
                } else {
                    infoVm.updateFollowing(customId)
                    infoVm.followerNum.value = infoVm.followerNum.value!! + 1
                    infoVm.isFollowing.value = !isFollowing
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(customId: String) =
            OtherFragment().apply {
                arguments = Bundle().apply {
                    putString(CUSTOM_ID, customId)
                }
            }
    }
}
