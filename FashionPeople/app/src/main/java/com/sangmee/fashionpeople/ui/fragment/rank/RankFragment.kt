package com.sangmee.fashionpeople.ui.fragment.rank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.dataSource.remote.RankImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.repository.RankImageRepositoryImpl
import com.sangmee.fashionpeople.databinding.FragmentRankBinding

class RankFragment : Fragment() {

    private lateinit var binding: FragmentRankBinding

    private val viewModel: RankViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return RankViewModel(RankImageRepositoryImpl(RankImageRemoteDataSourceImpl())) as T
            }
        }).get(RankViewModel::class.java)
    }

    private val dateAdapter: DateAdapter by lazy {
        DateAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rank, container, false)
        binding.lifecycleOwner = this@RankFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserve()
    }

    private fun initRecyclerView() {
        binding.rvDate.adapter = dateAdapter
    }

    private fun initObserve() {
        viewModel.dates.observe(viewLifecycleOwner, Observer {
            dateAdapter.setCustomDates(it)
        })
    }

    override fun onDestroy() {
        viewModel.clearDisposable()
        super.onDestroy()
    }

}
