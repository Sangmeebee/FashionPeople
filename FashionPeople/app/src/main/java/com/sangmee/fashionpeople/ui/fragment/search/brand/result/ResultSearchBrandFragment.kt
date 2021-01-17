package com.sangmee.fashionpeople.ui.fragment.search.brand.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentResultSearchBrandBinding
import com.sangmee.fashionpeople.ui.fragment.search.brand.result.content.SearchRecentBrandContentFragment
import com.sangmee.fashionpeople.ui.fragment.search.brand.result.content.SearchScoreBrandContentFragment
import kotlinx.android.synthetic.main.fragment_result_search_brand.*

private const val ARG_PARAM1 = "param1"

class ResultSearchBrandFragment : Fragment() {
    private var query: String? = null
    private lateinit var binding: FragmentResultSearchBrandBinding

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
        return inflater.inflate(R.layout.fragment_result_search_brand, container, false)?.apply {
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
        val searchScoreBrandContentFragment = SearchScoreBrandContentFragment.newInstance(query!!)
        val searchRecentBrandContentFragment = SearchRecentBrandContentFragment.newInstance(query!!)

        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_container, searchScoreBrandContentFragment).commit()
        tl_sort.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab!!.position
                var selected: Fragment? = null
                selected = when (position) {
                    0 -> searchScoreBrandContentFragment
                    else -> searchRecentBrandContentFragment
                }
                childFragmentManager.beginTransaction()
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
            ResultSearchBrandFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}
