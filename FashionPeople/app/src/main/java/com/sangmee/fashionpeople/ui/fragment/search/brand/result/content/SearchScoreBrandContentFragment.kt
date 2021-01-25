package com.sangmee.fashionpeople.ui.fragment.search.brand.result.content

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
import com.sangmee.fashionpeople.databinding.FragmentSearchScoreBrandContentBinding
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.detail.DetailFragment


private const val ARG_PARAM1 = "param1"

class SearchScoreBrandContentFragment : Fragment(), SearchScoreBrandContentAdapter.OnClickListener {

    private var query: String? = null
    private lateinit var binding: FragmentSearchScoreBrandContentBinding
    private val vm by viewModels<SearchScoreBrandContentViewModel>()
    private val searchBrandContentAdapter by lazy { SearchScoreBrandContentAdapter() }

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
        return inflater.inflate(R.layout.fragment_search_score_brand_content, container, false)
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
        query?.let { vm.callScoreBrandImages(it) }
    }

    private fun initViewModel() {
        vm.isComplete.observe(viewLifecycleOwner, Observer {
            crossfade()
        })

        vm.scoreBrandImages.observe(viewLifecycleOwner, Observer {
            searchBrandContentAdapter.setFeedImages(it)
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
            adapter = searchBrandContentAdapter
            searchBrandContentAdapter.onClickListener = this@SearchScoreBrandContentFragment
        }
    }

    override fun onClickImage(feedImages: List<FeedImage>, position: Int) {
        vm.scoreBrandImages.value?.let {
            (activity as MainActivity).replaceFragmentUseBackStack(
                DetailFragment(
                    feedImages,
                    position
                )
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(query: String) =
            SearchScoreBrandContentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, query)
                }
            }
    }
}
