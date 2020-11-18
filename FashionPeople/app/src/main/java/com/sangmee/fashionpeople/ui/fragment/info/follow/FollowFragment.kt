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

class FollowFragment : Fragment() {
    private var fragmentId: Int? = null
    private lateinit var binding: FragmentFollowBinding
    private val vm by activityViewModels<InfoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.lifecycleOwner = viewLifecycleOwner
            binding.vm = vm
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTabLayout()
    }

    private fun setTabLayout() {
        //tablayout 세팅
        vp_follow.apply {
            adapter = FollowViewPagerAdapter(this@FollowFragment)
        }
        fragmentId?.let { vp_follow.post { vp_follow.currentItem = it } }

        TabLayoutMediator(tl_container, vp_follow) { tab, position ->
            when (position) {
                0 -> tab.text =
                    "팔로워 ${vm.followerNum.value}명"
                else -> tab.text = "팔로잉 ${vm.followingNum.value}명"
            }
        }.attach()
    }

    companion object {
        fun newInstance(fragmentId: Int) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, fragmentId)
                }
            }
    }
}
