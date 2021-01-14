package com.sangmee.fashionpeople.ui.fragment.search.style.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentResultSearchStyleBinding
import com.sangmee.fashionpeople.ui.fragment.search.style.result.content.SearchRecentStyleContentFragment
import com.sangmee.fashionpeople.ui.fragment.search.style.result.content.SearchScoreStyleContentFragment
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
        val searchScoreStyleContentFragment = SearchScoreStyleContentFragment.newInstance(query!!)
        val searchRecentStyleContentFragment = SearchRecentStyleContentFragment.newInstance(query!!)

        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_container, searchScoreStyleContentFragment).commit()
        tl_sort.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab!!.position
                var selected: Fragment? = null
                selected = when (position) {
                    0 -> searchScoreStyleContentFragment
                    else -> searchRecentStyleContentFragment
                }
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_container, selected).commit()

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
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
