package com.sangmee.fashionpeople.ui.fragment.info.other

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentOtherBinding
import com.sangmee.fashionpeople.observer.FollowViewModel
import com.sangmee.fashionpeople.observer.InfoViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.info.content.ViewPagerAdapter
import com.sangmee.fashionpeople.ui.fragment.info.follow.FollowFragment
import kotlinx.android.synthetic.main.fragment_info.*

private const val CUSTOM_ID = "param1"
private const val FRAGMENT_ID = "param2"

class OtherFragment : Fragment() {
    private var customId: String? = null
    private var fragmentId: Int? = null

    //otherfragment에서 내 계정 인지 판단
    var isMe = false

    lateinit var binding: FragmentOtherBinding
    private val followVm by activityViewModels<FollowViewModel>()
    private val infoVm by activityViewModels<InfoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            customId = it.getString(CUSTOM_ID)
            if (customId == infoVm.customId) {
                isMe = true
            }
            fragmentId = it.getInt(FRAGMENT_ID)
            Log.d("FSP_CUSTOM_ID", customId.toString())
            Log.d("FSP_FRAGMENT_ID", fragmentId.toString())
            fragmentId?.let { fragmentId -> findIsFollowing(fragmentId) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        customId?.let { infoVm.callProfile(it) }
        return inflater.inflate(R.layout.fragment_other, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.isMe = isMe
            binding.apply {
                customId = this@OtherFragment.customId
                otherVm = this@OtherFragment.infoVm
                fragmentId = this@OtherFragment.fragmentId
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
            if (it == 0) {
                btnForFollower()
            } else {
                btnForFollowing()
            }
        })
        infoVm.callActivity.observe(viewLifecycleOwner, Observer {
            (activity as MainActivity).replaceFragmentUseBackStack(
                FollowFragment.newInstance(
                    it,
                    customId!!
                )
            )
        })
    }

    private fun findIsFollowing(fragmentId: Int) {
        if (fragmentId == 0) {
            followVm.isFollowingsFollower.value?.let { isFollowings ->
                isFollowings[customId]?.let { infoVm.isFollowing.value = it }
            }
        } else {
            followVm.isFollowingsFollowing.value?.let { isFollowings ->
                isFollowings[customId]?.let { infoVm.isFollowing.value = it }
            }
        }
    }

    private fun btnForFollower() {
        customId?.let { customId ->
            followVm.isFollowingsFollower.value?.let { isFollowings ->
                isFollowings[customId]?.let { isFollowing ->
                    if (isFollowing) {
                        followVm.deleteFollowing(customId)
                        infoVm.followerNum.value?.let { infoVm.followerNum.value = it - 1 }
                    } else {
                        followVm.updateFollowing(customId)
                        infoVm.followerNum.value?.let { infoVm.followerNum.value = it + 1 }
                    }
                    isFollowings[customId] = !isFollowing
                    followVm.isFollowingsFollower.value = isFollowings
                }
            }

            followVm.isFollowingsFollowing.value?.let { isFollowings ->
                isFollowings[customId]?.let { isFollowing ->
                    isFollowings[customId] = !isFollowing
                    followVm.isFollowingsFollowing.value = isFollowings
                }
            }
        }
        infoVm.isFollowing.value?.let { infoVm.isFollowing.value = !it }
    }

    private fun btnForFollowing() {
        customId?.let { customId ->
            followVm.isFollowingsFollowing.value?.let { isFollowings ->
                isFollowings[customId]?.let { isFollowing ->
                    isFollowings[customId] = !isFollowing
                    if (isFollowing) {
                        followVm.deleteFollowing(customId)
                        infoVm.followerNum.value?.let { infoVm.followerNum.value = it - 1 }
                    } else {
                        followVm.updateFollowing(customId)
                        infoVm.followerNum.value?.let { infoVm.followerNum.value = it + 1 }
                    }
                    followVm.isFollowingsFollowing.value = isFollowings
                }
            }

            followVm.isFollowingsFollower.value?.let { isFollowings ->
                isFollowings[customId]?.let { isFollowing ->
                    isFollowings[customId] = !isFollowing
                    followVm.isFollowingsFollower.value = isFollowings
                }
            }
        }
        infoVm.isFollowing.value?.let { infoVm.isFollowing.value = !it }
    }

    companion object {
        @JvmStatic
        fun newInstance(customId: String, fragmentId: Int) =
            OtherFragment().apply {
                arguments = Bundle().apply {
                    putString(CUSTOM_ID, customId)
                    putInt(FRAGMENT_ID, fragmentId)
                }
            }
    }
}
