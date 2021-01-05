package com.sangmee.fashionpeople.ui.fragment.info.other

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentOtherBinding
import com.sangmee.fashionpeople.observer.InfoViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.info.ReviseUserInfoActivity
import com.sangmee.fashionpeople.ui.fragment.info.SettingActivity
import com.sangmee.fashionpeople.ui.fragment.info.follow.FollowFragment
import com.sangmee.fashionpeople.ui.fragment.info.image_content.ViewPagerAdapter
import com.sangmee.fashionpeople.ui.login.LoginActivity
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

        //툴바 세팅
        setToolbar(binding.tbProfile)
        setHasOptionsMenu(true)
    }

    private fun setTabLayout() {
        viewPager.adapter = ViewPagerAdapter(this, customId!!) {
            infoVm.isCallFeedImageComplete.value = it
        }

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

        infoVm.profileReviseBtnEvent.observe(this, Observer {
            val intent = Intent(context, ReviseUserInfoActivity::class.java)
            intent.putExtra("nick_name", infoVm.userName.value.toString())
            intent.putExtra("gender", infoVm.gender.value.toString())
            infoVm.profileImgName.value?.let {
                intent.putExtra("profile_image_name", it)
            }
            infoVm.introduce.value?.let {
                intent.putExtra("introduce", it)
            }
            startActivityForResult(intent, REVISE_PROFILE)
            requireActivity().overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        })

        infoVm.isCallFeedImageComplete.observe(viewLifecycleOwner, Observer {
            if(it){
                if(infoVm.isCallProfileComplete.value!!){
                    binding.pbLoading.isVisible = false
                    binding.clContainer.isVisible = true
                }
            }
        })
        infoVm.isCallProfileComplete.observe(viewLifecycleOwner, Observer {
            if(it){
                if(infoVm.isCallFeedImageComplete.value!!){
                    binding.pbLoading.isVisible = false
                    binding.clContainer.isVisible = true
                }
            }
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGOUT_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            activity?.finish()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        if (requestCode == REVISE_PROFILE && resultCode == AppCompatActivity.RESULT_OK) {
            infoVm.userName.value = data?.getStringExtra("nick_name")
            infoVm.gender.value = data?.getStringExtra("gender")
            infoVm.introduce.value = data?.getStringExtra("introduce")
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
                val intent = Intent(context, SettingActivity::class.java)
                startActivityForResult(intent, LOGOUT_CODE)
                requireActivity().overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
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
        private const val LOGOUT_CODE = 270
        private const val REVISE_PROFILE = 280

        @JvmStatic
        fun newInstance(customId: String) =
            OtherFragment().apply {
                arguments = Bundle().apply {
                    putString(CUSTOM_ID, customId)
                }
            }
    }
}
