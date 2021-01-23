package com.sangmee.fashionpeople.ui.fragment.search.style.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentResultSearchStyleBinding
import kotlinx.android.synthetic.main.fragment_result_search_style.*

private const val ARG_PARAM1 = "param1"

class ResultSearchStyleFragment : Fragment() {
    private var query: String? = null
    private lateinit var binding: FragmentResultSearchStyleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            query = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result_search_style, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.query = "#$query"
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTabLayout()
    }

    private fun setTabLayout() {
        viewPager.adapter = ResultSearchStyleViewPagerAdapter(this, query)

        TabLayoutMediator(tl_sort, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setIcon(R.drawable.tab_popular_selector)
                }
                else -> {
                    tab.setIcon(R.drawable.tab_recent_selector)
                }
            }
        }.attach()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            ResultSearchStyleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}
