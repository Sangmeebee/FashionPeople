package com.sangmee.fashionpeople.ui.fragment.search.style

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
import com.sangmee.fashionpeople.ui.fragment.search.style.result.ResultSearchStyleFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_search_style.*

class SearchStyleFragment : Fragment(), OnStyleItemSelectedInterface {

    private val searchStyleAdapter by lazy { SearchStyleAdapter(this) }
    private val popularSearchStyleAdapter by lazy { SearchStyleAdapter(this) }
    private val vm by activityViewModels<SearchStyleViewModel>()
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
        return inflater.inflate(R.layout.fragment_search_style, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        initView()
        setViewModel()
    }

    private fun setViewModel() {
        vm.styleList.observe(viewLifecycleOwner, Observer {
            searchStyleAdapter.setStyleList(it)
        })

        vm.popularList.observe(viewLifecycleOwner, Observer {
            popularSearchStyleAdapter.setStyleList(it)
        })

        vm.isComplete.observe(viewLifecycleOwner, Observer {
            rv_style.visibility = View.VISIBLE
            ll_recent_container.visibility = View.INVISIBLE
        })

        vm.isEmpty.observe(viewLifecycleOwner, Observer {
            ll_recent_container.apply {
                visibility = View.VISIBLE
                alpha = 1f
            }
            rv_style.visibility = View.INVISIBLE
        })
    }

    private fun setRecyclerView() {
        rv_style.apply {
            setHasFixedSize(true)
            adapter = searchStyleAdapter
        }

        rv_recent_search.apply {
            setHasFixedSize(true)
            adapter = popularSearchStyleAdapter
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
            ResultSearchStyleFragment.newInstance(query), mainVm.tagName.value!!
        )
        searchVm.closeKeyBoard.call()
    }

    override fun onPause() {
        compositeDisposable.clear()
        vm.unbindViewModel()
        super.onPause()
    }
}
