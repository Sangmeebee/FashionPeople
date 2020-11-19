package com.sangmee.fashionpeople.ui.fragment.info

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.databinding.FragmentInfoBinding
import com.sangmee.fashionpeople.observer.InfoViewModel
import com.sangmee.fashionpeople.ui.LoginActivity
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.SettingActivity
import com.sangmee.fashionpeople.ui.fragment.info.content.ViewPagerAdapter
import com.sangmee.fashionpeople.ui.fragment.info.follow.FollowFragment
import kotlinx.android.synthetic.main.fragment_info.*

class InfoFragment : Fragment() {

    val customId by lazy { GlobalApplication.prefs.getString("custom_id", "") }
    lateinit var binding: FragmentInfoBinding
    private val vm by activityViewModels<InfoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.vm = vm
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelCallback()
        vm.callProfile(vm.customId)
        //tablayout 세팅
        viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(tl_container, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "게시물"
                else -> tab.text = "저장함"
            }
        }.attach()

        btn_setting.setOnClickListener {
            val intent = Intent(context, SettingActivity::class.java)
            startActivityForResult(intent, LOGOUT_CODE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGOUT_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            activity?.finish()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun viewModelCallback() {

        vm.callActivity.observe(viewLifecycleOwner, Observer {
            (activity as MainActivity).replaceFragmentUseBackStack(FollowFragment.newInstance(it, customId))
        })
    }
    companion object {
        private const val LOGOUT_CODE = 210
    }


}
