package com.sangmee.fashionpeople.ui.fragment.search.brand

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.search.brand.result.ResultSearchBrandFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_search_brand.*

class SearchBrandFragment : Fragment(), OnBrandItemSelectedInterface {

    private val searchBrandAdapter by lazy { SearchBrandAdapter(this) }
    private val recentSearchBrandAdapter by lazy { RecentSearchBrandAdapter(this) }
    private val vm by activityViewModels<SearchBrandViewModel>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_brand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Sangmeebee", "brand viewCreated")
        vm.callRecentList()
        setViewModel()
        initView()
        setRecyclerView()
    }

    private fun setViewModel() {
        vm.brandList.observe(viewLifecycleOwner, Observer {
            Log.d("Sangmeebee", "brand brandList")

            val brandList = ArrayList<String>()
            val numList = ArrayList<Int>()
            for (brand in it) {
                brandList.add(brand.brandName)
                numList.add(brand.postNum)
            }
            searchBrandAdapter.setBrandList(brandList, numList)
        })

        vm.recentList.observe(viewLifecycleOwner, Observer {
            recentSearchBrandAdapter.setBrandList(it)
        })

        vm.isComplete.observe(viewLifecycleOwner, Observer {
            rv_brand.visibility = View.VISIBLE
            ll_recent_container.visibility = View.INVISIBLE
        })

        vm.isEmpty.observe(viewLifecycleOwner, Observer {
            ll_recent_container.apply {
                vm.recentList.value?.let { recentList ->
                    if (recentList.isNotEmpty()) {
                        visibility = View.VISIBLE
                        alpha = 1f
                    }
                }
            }
            recentSearchBrandAdapter.setBrandList(vm.recentList.value!!)
            rv_brand.visibility = View.INVISIBLE
        })
    }

    private fun setRecyclerView() {
        val height = getDisplayHeight()
        rv_brand.apply {
            setHasFixedSize(true)
            minimumHeight = height
            adapter = searchBrandAdapter
        }

        rv_recent_search.apply {
            setHasFixedSize(true)
            adapter = recentSearchBrandAdapter
        }
    }

    private fun initView() {

        btn_clear.setOnClickListener {
            vm.clearRecentList()
            vm.callRecentList()
        }

        ll_recent_container.apply {
            vm.recentList.value?.let { recentList ->
                if (recentList.isNotEmpty()) {
                    visibility = View.VISIBLE
                    alpha = 1f
                }
            }
        }
    }

    private fun getDisplayHeight(): Int {
        val display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        var height = size.y
        val resourceId = resources.getIdentifier(
            "design_bottom_navigation_height",
            "dimen",
            requireContext().packageName
        )
        if (resourceId > 0) {
            val navigationBarHeight = resources.getDimensionPixelSize(resourceId)
            height -= navigationBarHeight
        }
        return height
    }

    override fun onItemSelected(query: String) {
        (activity as MainActivity).replaceFragmentUseBackStack(
            ResultSearchBrandFragment.newInstance(query)
        )
        vm.postRecentList(query)
    }

    override fun onClickCancelBtn(query: String) {
        vm.deleteRecentList(query)
        vm.callRecentList()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        vm.unbindViewModel()
        super.onDestroy()
    }
}
