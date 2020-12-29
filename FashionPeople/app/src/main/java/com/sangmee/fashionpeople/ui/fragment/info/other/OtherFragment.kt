package com.sangmee.fashionpeople.ui.fragment.info.other

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentOtherBinding
import com.sangmee.fashionpeople.observer.InfoViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.info.follow.FollowFragment
import com.sangmee.fashionpeople.ui.fragment.info.image_content.ViewPagerAdapter
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

        //자기소개글 있는지 판단
        infoVm.introduce.value?.let {
            if (it.isNotEmpty()) {
                infoVm.isInvisible.value = true
            }
        }
        binding.isInvisible = infoVm.isInvisible.value

        //툴바 세팅
        setToolbar(binding.tbProfile)
        setHasOptionsMenu(true)
    }

    private fun setTabLayout() {
        viewPager.adapter = ViewPagerAdapter(this, customId!!)

        TabLayoutMediator(tl_container, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setIcon(R.drawable.photo_library_selector)
                }
                else -> {
                    tab.setIcon(R.drawable.photo_saved_selector)
                }
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


    //메뉴 버튼 세팅
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.setting_toolbar, menu)
    }

    //메뉴 버튼 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> {
                Toast.makeText(context, "click", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar(toolbar: Toolbar) {
        (activity as MainActivity).setSupportActionBar(toolbar)
        (activity as MainActivity).supportActionBar?.run {
            setDisplayShowTitleEnabled(false)
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
