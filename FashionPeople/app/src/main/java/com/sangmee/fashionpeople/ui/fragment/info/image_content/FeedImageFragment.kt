package com.sangmee.fashionpeople.ui.fragment.info.image_content

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentFeedImageBinding
import com.sangmee.fashionpeople.observer.FeedImageViewModel
import com.sangmee.fashionpeople.observer.InfoViewModel

class FeedImageFragment : Fragment() {

    private var userId: String? = null
    private lateinit var binding: FragmentFeedImageBinding
    private val vm: FeedImageViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return FeedImageViewModel() as T
            }
        }).get(FeedImageViewModel::class.java)
    }

    private var isEmpty = false
    private val feedImageAdapter by lazy {
        userId?.let {
            FeedImageAdapter(it)
        }
    }

    private lateinit var isComplete : (Boolean) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString("custom_id")
        }
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
        userId?.let { vm.callFeedImages(it) }
        setRecyclerView()
    }

    private fun setRecyclerView() {
        val height = getDisplayHeight()
        binding.rvUserImage.apply {
            setHasFixedSize(true)
            minimumHeight = height
            adapter = feedImageAdapter
        }
    }

    private fun viewModelCallback() {
        vm.feedImages.observe(viewLifecycleOwner, Observer {
            isEmpty = it.isEmpty()
            binding.isEmpty = isEmpty
            feedImageAdapter?.setFeedImages(it)
        })

        vm.isComplete.observe(viewLifecycleOwner, Observer {
            if(it) {
                isComplete(true)
            }
        })
    }

    private fun getDisplayHeight(): Int {
        val display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        var height = size.y
        val tv = TypedValue()
        if (requireActivity().theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            val actionBarHeight = TypedValue.complexToDimensionPixelSize(
                tv.data,
                resources.displayMetrics
            )
            height -= actionBarHeight
        }
        val resourceId = resources.getIdentifier(
            "design_bottom_navigation_height",
            "dimen",
            requireContext().packageName
        )
        if (resourceId > 0) {
            var navigationBarHeight = resources.getDimensionPixelSize(resourceId)
            height -= navigationBarHeight
        }
        return height
    }

    override fun onDestroy() {
        vm.unBindDisposable()
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        fun newInstance(userId: String, isComplete : (Boolean) -> Unit) =
            FeedImageFragment().apply {
                arguments = Bundle().apply {
                    putString("custom_id", userId)
                }
                this.isComplete = isComplete
            }
    }
}
