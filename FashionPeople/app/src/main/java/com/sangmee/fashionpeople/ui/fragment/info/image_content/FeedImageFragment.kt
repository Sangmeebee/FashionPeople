package com.sangmee.fashionpeople.ui.fragment.info.image_content

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
import com.sangmee.fashionpeople.databinding.FragmentFeedImageBinding
import com.sangmee.fashionpeople.observer.FeedImageViewModel
import kotlinx.android.synthetic.main.fragment_feed_image.*

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
        rv_user_image.apply {
            setHasFixedSize(true)
            adapter = feedImageAdapter
        }
    }

    private fun viewModelCallback() {
        vm.feedImages.observe(viewLifecycleOwner, Observer {
            isEmpty = it.isEmpty()
            binding.isEmpty = isEmpty
            feedImageAdapter?.setFeedImages(it)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(userId: String) =
            FeedImageFragment().apply {
                arguments = Bundle().apply {
                    putString("custom_id", userId)
                }
            }
    }
}