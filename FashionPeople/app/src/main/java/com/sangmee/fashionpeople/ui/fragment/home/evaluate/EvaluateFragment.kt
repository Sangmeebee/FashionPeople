package com.sangmee.fashionpeople.ui.fragment.home.evaluate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

    private val homeFeedAdapter: HomeFeedAdapter by lazy {
        HomeFeedAdapter()
    }

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
        binding.vpEvaluate.apply {
            adapter = homeFeedAdapter
        }
        initObserve()
    }

    private fun initObserve() {
        viewModel.feedImages.observe(viewLifecycleOwner, Observer {
            homeFeedAdapter.setFeedImages(it)
        })
    }

    override fun onDestroy() {
        viewModel.clearDisposable()
        super.onDestroy()
    }
}