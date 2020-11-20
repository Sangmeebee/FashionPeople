package com.sangmee.fashionpeople.ui.fragment.info.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentFollowBinding
import com.sangmee.fashionpeople.observer.InfoViewModel
import kotlinx.android.synthetic.main.fragment_follow.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FollowFragment : Fragment() {
    private var fragmentId: Int? = null
    private var userId: String? = null
    private var followerNum: Int? = null
    private var followingNum: Int? = null
    private var userName: String? = null
    private lateinit var binding: FragmentFollowBinding
    private val vm by activityViewModels<InfoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentId = it.getInt(ARG_PARAM1)
            userId = it.getString(ARG_PARAM2)
            userName = vm.userName.value
            followerNum = vm.followerNum.value
            followingNum = vm.followingNum.value
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.userName = userName
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTabLayout()

    }

    private fun setTabLayout() {

        vp_follow.apply {
            adapter = FollowViewPagerAdapter(this@FollowFragment, userId!!)
        }

        //tablayout 세팅
        TabLayoutMediator(tl_container, vp_follow) { tab, position ->
            when (position) {
                0 -> tab.text =
                    "팔로워 ${followerNum}명"
                else -> tab.text = "팔로잉 ${followingNum}명"
            }
        }.attach()

        //tab 몇번째 화면인지 세팅
        fragmentId?.let {
            binding.vpFollow.post { binding.vpFollow.currentItem = it }
        }
    }

    companion object {
        fun newInstance(fragmentId: Int, userId: String) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, fragmentId)
                    putString(ARG_PARAM2, userId)
                }
            }
    }
}
