package com.sangmee.fashionpeople.ui.fragment.search.style

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
import com.sangmee.fashionpeople.ui.fragment.search.style.result.ResultSearchStyleFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_search_style.*

class SearchStyleFragment : Fragment(), OnStyleItemSelectedInterface {

    private val searchStyleAdapter by lazy { SearchStyleAdapter(this) }
    private val recentSearchStyleAdapter by lazy { RecentSearchStyleAdapter(this) }
    private val vm by activityViewModels<SearchStyleViewModel>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_style, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Sangmeebee", "style viewCreated")
        vm.callRecentList()
        setRecyclerView()
        initView()
        setViewModel()
    }

    private fun setViewModel() {
        vm.styleList.observe(viewLifecycleOwner, Observer {
            Log.d("Sangmeebee", "style stylelist")

            val styleList = ArrayList<String>()
            val numList = ArrayList<Int>()
            for (style in it) {
                styleList.add(style.styleName)
                numList.add(style.postNum)
            }
            searchStyleAdapter.setStyleList(styleList, numList)
        })

        vm.recentList.observe(viewLifecycleOwner, Observer {
            recentSearchStyleAdapter.setStyleList(it)
        })

        vm.isComplete.observe(viewLifecycleOwner, Observer {
            rv_style.visibility = View.VISIBLE
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
            recentSearchStyleAdapter.setStyleList(vm.recentList.value!!)
            rv_style.visibility = View.INVISIBLE
        })
    }

    private fun setRecyclerView() {
        val height = getDisplayHeight()
        rv_style.apply {
            setHasFixedSize(true)
            minimumHeight = height
            adapter = searchStyleAdapter
        }

        rv_recent_search.apply {
            setHasFixedSize(true)
            adapter = recentSearchStyleAdapter
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
            ResultSearchStyleFragment.newInstance(query)
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
