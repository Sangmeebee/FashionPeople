package com.sangmee.fashionpeople.ui.fragment.info.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.observer.FeedImageViewModel
import kotlinx.android.synthetic.main.fragment_feed_image.*

class FeedImageFragment : Fragment() {

    private val vm by activityViewModels<FeedImageViewModel>()
    private val feedImageAdapter by lazy {
        FeedImageAdapter(vm.customId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.callFeedImages()
        viewModelCallback()
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
            feedImageAdapter.setFeedImages(it)
        })
    }
}
