package com.sangmee.fashionpeople.ui.fragment.search.style.result.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.FragmentSearchRecentStyleContentBinding
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.search.detail.SearchDetailFragment


private const val ARG_PARAM1 = "param1"

class SearchRecentStyleContentFragment : Fragment(),
    SearchRecentStyleContentAdapter.OnClickListener {

    private var query: String? = null
    private lateinit var binding: FragmentSearchRecentStyleContentBinding
    private val vm by viewModels<SearchRecentStyleContentViewModel>()
    private val searchRecentStyleContentAdapter by lazy { SearchRecentStyleContentAdapter() }

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
        return inflater.inflate(R.layout.fragment_search_recent_style_content, container, false)
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
        query?.let { vm.callRecentStyleImages(it) }
    }

    private fun initViewModel() {
        vm.isComplete.observe(viewLifecycleOwner, Observer {
            crossfade()
        })
        vm.recentStyleImages.observe(viewLifecycleOwner, Observer {
            searchRecentStyleContentAdapter.setFeedImages(it)
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
            adapter = searchRecentStyleContentAdapter
            searchRecentStyleContentAdapter.onClickListener = this@SearchRecentStyleContentFragment
        }
    }

    override fun onClickImage(feedImages: List<FeedImage>, position: Int) {
        vm.recentStyleImages.value?.let {
            (activity as MainActivity).replaceFragmentUseBackStack(
                SearchDetailFragment(
                    feedImages,
                    position
                )
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(query: String) =
            SearchRecentStyleContentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, query)
                }
            }
    }
}
