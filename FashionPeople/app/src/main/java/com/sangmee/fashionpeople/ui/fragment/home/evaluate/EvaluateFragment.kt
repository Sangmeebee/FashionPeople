package com.sangmee.fashionpeople.ui.fragment.home.evaluate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentEvaluateBinding
import com.sangmee.fashionpeople.ui.fragment.home.HomeFeedAdapter

class EvaluateFragment : Fragment() {

    private lateinit var binding: FragmentEvaluateBinding

    private val viewModel: EvaluateViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return EvaluateViewModel() as T
            }
        }).get(EvaluateViewModel::class.java)
    }

    private lateinit var homeFeedAdapter: HomeFeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_evaluate, container, false)
        binding.lifecycleOwner = this@EvaluateFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
        initObserve()
    }

    private fun initViewPager() {
        homeFeedAdapter = HomeFeedAdapter()
        binding.vpEvaluate.apply {
            adapter = homeFeedAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL
        }
    }

    private fun initObserve() {
        viewModel.feedImages.observe(this@EvaluateFragment, Observer {
            homeFeedAdapter.setFeedImages(it)
        })
    }

    override fun onDestroy() {
        viewModel.clearDisposable()
        super.onDestroy()
    }
}