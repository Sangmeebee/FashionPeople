package com.sangmee.fashionpeople.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.tabs.TabLayout
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentHomeBinding
import com.sangmee.fashionpeople.ui.fragment.home.evaluate.EvaluateFragment
import com.sangmee.fashionpeople.ui.fragment.home.following.FollowingFragment
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var manager: FragmentManager
    private lateinit var evaluateFragment: EvaluateFragment
    private lateinit var followingFragment: FollowingFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manager = childFragmentManager
        evaluateFragment = EvaluateFragment()
        followingFragment = FollowingFragment()

        manager.beginTransaction().replace(R.id.fl_home, evaluateFragment).commit()

        setTabLayout()
    }

    private fun setTabLayout() {

        tl_home.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> {
                        if(!evaluateFragment.isAdded){
                            manager.beginTransaction().add(R.id.fl_home, evaluateFragment).commit()
                        }
                        manager.beginTransaction().show(evaluateFragment).commit()
                        manager.beginTransaction().hide(followingFragment).commit()
                    }
                    else -> {
                        if(!followingFragment.isAdded){
                            manager.beginTransaction().add(R.id.fl_home, followingFragment).commit()
                        }
                        manager.beginTransaction().show(followingFragment).commit()
                        manager.beginTransaction().hide(evaluateFragment).commit()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
}

