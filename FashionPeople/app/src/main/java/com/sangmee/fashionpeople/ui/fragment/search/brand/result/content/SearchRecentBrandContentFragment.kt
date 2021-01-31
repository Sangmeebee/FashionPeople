package com.sangmee.fashionpeople.ui.fragment.search.brand.result.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.FragmentSearchRecentBrandContentBinding
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.detail.DetailFragment


private const val ARG_PARAM1 = "param1"

class SearchRecentBrandContentFragment : Fragment(),
    SearchRecentBrandContentAdapter.OnClickListener {

    private var query: String? = null
    private lateinit var binding: FragmentSearchRecentBrandContentBinding
    private val vm by viewModels<SearchRecentBrandContentViewModel>()
    private val mainVm by activityViewModels<MainViewModel>()

    private lateinit var searchRecentBrandContentAdapter: SearchRecentBrandContentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            query = it.getString(ARG_PARAM1)
        }
        query?.let { vm.callRecentBrandImages(it) }
        searchRecentBrandContentAdapter = SearchRecentBrandContentAdapter()
        vm.isAdded.value = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_recent_brand_content, container, false)
            ?.apply {
                binding = DataBindingUtil.bind(this)!!
                binding.lifecycleOwner = viewLifecycleOwner
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
        setRecyclerView()
    }

    private fun initView() {
        vm.isAdded.value?.let {
            if (it) {
                binding.rvSearchImage.isVisible = true
            }
        }
    }

    private fun initViewModel() {
        vm.isComplete.observe(viewLifecycleOwner, Observer {
            crossfade()
            vm.isAdded.value = true
        })
        vm.recentBrandImages.observe(viewLifecycleOwner, Observer {
            searchRecentBrandContentAdapter.setFeedImages(it)
        })
    }


    private fun crossfade() {
        binding.rvSearchImage.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(500L)
                .setListener(null)
        }
    }

    private fun setRecyclerView() {
        binding.rvSearchImage.apply {
            setHasFixedSize(true)
            adapter = searchRecentBrandContentAdapter
            searchRecentBrandContentAdapter.onClickListener = this@SearchRecentBrandContentFragment
        }
    }

    override fun onClickImage(feedImages: List<FeedImage>, position: Int) {
        vm.recentBrandImages.value?.let {
            (activity as MainActivity).replaceFragmentUseTagBackStack(
                DetailFragment(
                    feedImages,
                    position
                ), mainVm.tagName.value!!
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(query: String) =
            SearchRecentBrandContentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, query)
                }
            }
    }
}
