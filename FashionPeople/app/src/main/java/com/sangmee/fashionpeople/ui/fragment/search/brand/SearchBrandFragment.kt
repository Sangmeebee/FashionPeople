package com.sangmee.fashionpeople.ui.fragment.search.brand

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding4.widget.textChanges
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.search.SearchContentFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.android.synthetic.main.fragment_search_brand.*
import java.util.concurrent.TimeUnit

class SearchBrandFragment : Fragment(), OnBrandItemSelectedInterface {

    private val searchBrandAdapter by lazy { SearchBrandAdapter(this) }
    private var brandList = arrayListOf<String>()
    private var postNumList = arrayListOf<Int>()
    private val vm by viewModels<SearchBrandViewModel>()
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
        setViewModel()
        initEditTextListener()
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


        vm.isComplete.observe(viewLifecycleOwner, Observer {
            crossfade()
        })
    }

    private fun setRecyclerView() {
        val height = getDisplayHeight()
        rv_brand.apply {
            setHasFixedSize(true)
            minimumHeight = height
            adapter = searchBrandAdapter
        }
    }

    private fun initEditTextListener() {

        et_brand_name.textChanges()
            .debounce(500L, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (!it.isNullOrEmpty()) {
                    vm.callBrand(it.toString())
                } else {
                    searchBrandAdapter.setBrandList(brandList, postNumList)
                }
            }.addTo(compositeDisposable)
    }

    private fun crossfade() {
        rv_brand?.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(500L)
                .setListener(null)
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
            SearchContentFragment.newInstance(query)
        )
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        vm.unbindViewModel()
        super.onDestroy()
    }
}
