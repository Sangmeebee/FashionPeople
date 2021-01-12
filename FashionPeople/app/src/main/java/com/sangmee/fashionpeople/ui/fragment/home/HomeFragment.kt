package com.sangmee.fashionpeople.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentHomeBinding
import com.sangmee.fashionpeople.ui.fragment.home.evaluate.EvaluateFragment
import com.sangmee.fashionpeople.ui.fragment.home.following.FollowingFragment
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTabLayout()
    }

    private fun setTabLayout() {
        val evaluateFragment = EvaluateFragment()
        val followingFragment = FollowingFragment()

        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_home, evaluateFragment).commit()
        tl_home.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab!!.position
                var selected: Fragment? = null
                selected = when (position) {
                    0 -> evaluateFragment
                    else -> followingFragment
                }
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fl_home, selected).commit()

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
}

