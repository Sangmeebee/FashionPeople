package com.sangmee.fashionpeople.ui.fragment.rank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayout
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentRankBinding
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.ui.fragment.rank.content.ManRankFragment
import com.sangmee.fashionpeople.ui.fragment.rank.content.WomanRankFragment

class RankFragment : Fragment() {

    private lateinit var binding: FragmentRankBinding
    private lateinit var manager: FragmentManager
    private lateinit var manRankFragment: ManRankFragment
    private lateinit var womanRankFragment: WomanRankFragment
    private val mainVm: MainViewModel by activityViewModels()
    private val rankVm by activityViewModels<RankViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rankVm.manIsAdded.value = false
        rankVm.womanIsAdded.value = false
        manRankFragment = ManRankFragment()
        womanRankFragment = WomanRankFragment()
        setTabIndex()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rank, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manager = childFragmentManager

        rankVm.rankPage.value?.let {
            if (it == 0) {
                rankVm.manIsAdded.value?.let { isAdded ->
                    if (!isAdded) {
                        manager.beginTransaction().add(R.id.fl_rank, manRankFragment).commit()
                    }
                }
                binding.tlContainer.getTabAt(0)?.select()
            } else {
                rankVm.womanIsAdded.value?.let { isAdded ->
                    if (!isAdded) {
                        manager.beginTransaction().add(R.id.fl_rank, womanRankFragment).commit()
                    }
                }
                binding.tlContainer.getTabAt(1)?.select()
            }
        }
        setTabLayout()
    }


    private fun setTabLayout() {
        binding.tlContainer.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> {
                        rankVm.manIsAdded.value?.let {
                            if (!it) {
                                manager.beginTransaction().add(R.id.fl_rank, manRankFragment)
                                    .commit()
                            }
                        }
                        manager.beginTransaction().show(manRankFragment).commit()
                        manager.beginTransaction().hide(womanRankFragment).commit()
                        rankVm.rankPage.value = 0
                    }
                    else -> {
                        rankVm.womanIsAdded.value?.let {
                            if (!it) {
                                manager.beginTransaction().add(R.id.fl_rank, womanRankFragment)
                                    .commit()
                            }
                        }
                        manager.beginTransaction().show(womanRankFragment).commit()
                        manager.beginTransaction().hide(manRankFragment).commit()
                        rankVm.rankPage.value = 1
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun setTabIndex() {
        val gender = mainVm.user.value?.gender.toString()
        if (gender == "남") {
            rankVm.rankPage.value = 0
        } else {
            rankVm.rankPage.value = 1
        }
    }
}
