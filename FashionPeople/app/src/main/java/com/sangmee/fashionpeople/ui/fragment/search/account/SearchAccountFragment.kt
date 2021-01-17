package com.sangmee.fashionpeople.ui.fragment.search.account

import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.info.other.OtherFragment
import com.sangmee.fashionpeople.ui.fragment.search.SearchViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_search_account.*

class SearchAccountFragment : Fragment(), OnAccountItemSelectedInterface {

    private val searchAccountAdapter by lazy { SearchAccountAdapter(this) }
    private val recentSearchAccountAdapter by lazy { RecentSearchAccountAdapter(this) }
    private val vm by activityViewModels<SearchAccountViewModel>()
    private val searchVm by activityViewModels<SearchViewModel>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.callRecentList()
        setViewModel()
        initView()
        setRecyclerView()
    }

    private fun setViewModel() {
        vm.userList.observe(viewLifecycleOwner, Observer {
            searchAccountAdapter.setAccountList(it)
        })

        vm.recentList.observe(viewLifecycleOwner, Observer {
            recentSearchAccountAdapter.setAccountList(it)
        })

        vm.isComplete.observe(viewLifecycleOwner, Observer {
            rv_account.visibility = View.VISIBLE
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
            recentSearchAccountAdapter.setAccountList(vm.recentList.value!!)
            rv_account.visibility = View.INVISIBLE
        })
    }

    private fun setRecyclerView() {
        val height = getDisplayHeight()
        rv_account.apply {
            setHasFixedSize(true)
            minimumHeight = height
            adapter = searchAccountAdapter
        }

        rv_recent_search.apply {
            setHasFixedSize(true)
            adapter = recentSearchAccountAdapter
        }
    }

    private fun initView() {

        btn_clear.setOnClickListener {
            vm.clearRecentList()
            vm.callRecentList()
        }

        searchVm.etText.value?.let {
            if (it == "")
                ll_recent_container.apply {
                    vm.recentList.value?.let { recentList ->
                        if (recentList.isNotEmpty()) {
                            visibility = View.VISIBLE
                            alpha = 1f
                        }
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

    override fun onItemSelected(user: FUser) {
        user.id?.let {
            (activity as MainActivity).replaceFragmentUseBackStack(
                OtherFragment.newInstance(it)
            )
            vm.callUser(it)
        }
    }

    override fun onClickCancelBtn(user: FUser) {
        vm.deleteRecentList(user)
        vm.callRecentList()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        vm.unbindViewModel()
        super.onDestroy()
    }
}
