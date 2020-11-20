package com.sangmee.fashionpeople.ui.fragment.info.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentFeedImageBinding
import com.sangmee.fashionpeople.observer.FeedImageViewModel
import kotlinx.android.synthetic.main.fragment_feed_image.*

class FeedImageFragment(private val userId: String) : Fragment() {

    private lateinit var binding: FragmentFeedImageBinding
    private val vm by activityViewModels<FeedImageViewModel>()
    private var isEmpty = false
    private val feedImageAdapter by lazy {
        FeedImageAdapter(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelCallback()
        return inflater.inflate(R.layout.fragment_feed_image, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.callFeedImages(userId)
        setRecyclerView()
    }

    private fun setRecyclerView() {
        rv_user_image.apply {
            setHasFixedSize(true)
            adapter = feedImageAdapter
        }
    }

    private fun viewModelCallback() {
        vm.feedImages.observe(viewLifecycleOwner, Observer {
            isEmpty = it.isEmpty()
            binding.isEmpty = isEmpty
            feedImageAdapter.setFeedImages(it)
        })
    }
}
