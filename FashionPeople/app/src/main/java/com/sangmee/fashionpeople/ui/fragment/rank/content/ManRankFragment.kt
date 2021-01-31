package com.sangmee.fashionpeople.ui.fragment.rank.content

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.FragmentManRankBinding
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.detail.DetailFragment
import com.sangmee.fashionpeople.ui.fragment.rank.RankViewModel

class ManRankFragment : Fragment() {

    private lateinit var binding: FragmentManRankBinding
    private lateinit var manRankAdapter: ManRankAdapter
    private val vm by activityViewModels<ManRankViewModel>()
    private val rankVm by activityViewModels<RankViewModel>()
    private val mainVm by activityViewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manRankAdapter = ManRankAdapter(::showDetail)
        vm.getRankImages()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_man_rank, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setRecyclerView()
        initView()
    }

    private fun initViewModel() {
        vm.dates.observe(viewLifecycleOwner, Observer {
            manRankAdapter.setCustomDates(it)
        })

        vm.isComplete.observe(viewLifecycleOwner, Observer {
            rankVm.manIsAdded.value = true
            crossfade()
        })
    }

    private fun initView() {
        rankVm.manIsAdded.value?.let {
            if (it) {
                binding.rvManRank.isVisible = true
                binding.pbLoading.isVisible = false
            }
        }
    }

    private fun setRecyclerView() {
        binding.rvManRank.apply {
            setHasFixedSize(true)
            adapter = manRankAdapter
        }
    }

    private fun crossfade() {
        binding.rvManRank.apply {
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

    private fun showDetail(feedImages: List<FeedImage>, position: Int) {
        Log.d("Sangmeebee", "showDetail")
        (activity as MainActivity).replaceFragmentUseTagBackStack(
            DetailFragment(feedImages, position), mainVm.tagName.value!!
        )
    }

    override fun onDestroy() {
        vm.clearDisposable()
        super.onDestroy()
    }
}
