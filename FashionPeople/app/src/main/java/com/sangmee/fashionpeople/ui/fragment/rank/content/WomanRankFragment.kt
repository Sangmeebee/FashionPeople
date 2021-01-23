package com.sangmee.fashionpeople.ui.fragment.rank.content

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentWomanRankBinding

class WomanRankFragment : Fragment() {

    private lateinit var binding: FragmentWomanRankBinding
    private val womanRankAdapter by lazy { WomanRankAdapter() }
    private val vm by viewModels<WomanRankViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_woman_rank, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setRecyclerView()
    }

    private fun initViewModel() {
        vm.dates.observe(viewLifecycleOwner, Observer {
            womanRankAdapter.setCustomDates(it)
        })

        vm.isComplete.observe(viewLifecycleOwner, Observer {
            crossfade()
        })
    }


    private fun setRecyclerView() {
        binding.rvWomanRank.apply {
            setHasFixedSize(true)
            adapter = womanRankAdapter
        }
    }

    private fun crossfade() {
        binding.rvWomanRank.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(500L)
                .setListener(null)
        }
        binding.pbLoading.animate()
            .alpha(0f)
            .setDuration(500L)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.pbLoading.visibility = View.GONE
                }
            })
    }

    override fun onDestroy() {
        vm.clearDisposable()
        super.onDestroy()
    }
}
