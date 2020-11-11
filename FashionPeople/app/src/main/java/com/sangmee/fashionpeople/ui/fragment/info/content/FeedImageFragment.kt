package com.sangmee.fashionpeople.ui.fragment.info.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentFeedImageBinding
import com.sangmee.fashionpeople.observer.FeedImageViewModel
import kotlinx.android.synthetic.main.fragment_feed_image.*

class FeedImageFragment : Fragment() {

    private val vm by activityViewModels<FeedImageViewModel>()
    private lateinit var binding: FragmentFeedImageBinding
    private val feedImageAdapter by lazy {
        FeedImageAdapter(vm.customId)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_image, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.lifecycleOwner = viewLifecycleOwner
            vm.callFeedImages()
            setRecyclerView()
            viewModelCallback()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_user_image.apply {
            adapter = feedImageAdapter
        }
    }

    private fun setRecyclerView() {
        binding.rvUserImage.apply {
            setHasFixedSize(true)
            adapter = feedImageAdapter
        }
    }

    private fun viewModelCallback() {
        vm.feedImages.observe(viewLifecycleOwner, Observer {
            feedImageAdapter.setFeedImages(it)
        })
    }
}
