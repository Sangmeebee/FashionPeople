package com.sangmee.fashionpeople.ui.fragment.rank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentRankBinding
import com.sangmee.fashionpeople.ui.fragment.rank.content.ManRankFragment
import com.sangmee.fashionpeople.ui.fragment.rank.content.WomanRankFragment

class RankFragment : Fragment() {

    private lateinit var binding: FragmentRankBinding
    private lateinit var manager: FragmentManager
    private val manRankFragment by lazy { ManRankFragment() }
    private val womanRankFragment by lazy { WomanRankFragment() }

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
        manager.beginTransaction().replace(R.id.fl_rank, manRankFragment).commit()
        setTabLayout()
    }


    private fun setTabLayout() {
        binding.tlContainer.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> {
                        if (!manRankFragment.isAdded) {
                            manager.beginTransaction().add(R.id.fl_rank, manRankFragment).commit()
                        }
                        manager.beginTransaction().show(manRankFragment).commit()
                        manager.beginTransaction().hide(womanRankFragment).commit()
                    }
                    else -> {
                        if (!womanRankFragment.isAdded) {
                            manager.beginTransaction().add(R.id.fl_rank, womanRankFragment).commit()
                        }
                        manager.beginTransaction().show(womanRankFragment).commit()
                        manager.beginTransaction().hide(manRankFragment).commit()
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
