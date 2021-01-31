package com.sangmee.fashionpeople.ui.fragment.search.brand

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.search.SearchViewModel
import com.sangmee.fashionpeople.ui.fragment.search.brand.result.ResultSearchBrandFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_search_brand.*

class SearchBrandFragment : Fragment(), OnBrandItemSelectedInterface {

    private val searchBrandAdapter by lazy { SearchBrandAdapter(this) }
    private val recentSearchBrandAdapter by lazy { SearchBrandAdapter(this) }
    private val vm by activityViewModels<SearchBrandViewModel>()
    private val searchVm by activityViewModels<SearchViewModel>()
    private val mainVm by activityViewModels<MainViewModel>()
    private val compositeDisposable = CompositeDisposable()


    override fun onResume() {
        super.onResume()
        vm.callPopularList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_brand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        initView()
        setRecyclerView()
    }

    private fun setViewModel() {

        vm.brandList.observe(viewLifecycleOwner, Observer {
            searchBrandAdapter.setBrandList(it)
        })

        vm.popularList.observe(viewLifecycleOwner, Observer {
            recentSearchBrandAdapter.setBrandList(it)
        })

        vm.isComplete.observe(viewLifecycleOwner, Observer {
            rv_brand.visibility = View.VISIBLE
            ll_recent_container.visibility = View.INVISIBLE
        })

        vm.isEmpty.observe(viewLifecycleOwner, Observer {
            ll_recent_container.apply {
                visibility = View.VISIBLE
                alpha = 1f
            }
            rv_brand.visibility = View.INVISIBLE
        })
    }

    private fun setRecyclerView() {
        rv_brand.apply {
            setHasFixedSize(true)
            adapter = searchBrandAdapter
        }

        rv_recent_search.apply {
            setHasFixedSize(true)
            adapter = recentSearchBrandAdapter
        }
    }

    private fun initView() {
        searchVm.etText.value?.let {
            if (it == "")
                ll_recent_container.apply {
                    visibility = View.VISIBLE
                    alpha = 1f
                }
        }
    }

    override fun onItemSelected(query: String) {
        (activity as MainActivity).replaceFragmentUseTagBackStack(
            ResultSearchBrandFragment.newInstance(query), mainVm.tagName.value!!
        )
        searchVm.closeKeyBoard.call()
    }

    override fun onPause() {
        compositeDisposable.clear()
        vm.unbindViewModel()
        super.onPause()
    }
}
