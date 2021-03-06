package com.sangmee.fashionpeople.ui.fragment.info.image_content

import android.graphics.Point
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.S3RemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.FragmentFeedImageBinding
import com.sangmee.fashionpeople.observer.FeedImageViewModel
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.detail.DetailFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class FeedImageFragment : Fragment(), FeedImageAdapter.OnClickListener {

    private var userId: String? = null
    private lateinit var binding: FragmentFeedImageBinding
    private val vm: FeedImageViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return FeedImageViewModel() as T
            }
        }).get(FeedImageViewModel::class.java)
    }
    private val compositeDisposable = CompositeDisposable()
    private val mainVm by activityViewModels<MainViewModel>()
    private val s3RemoteDataSource by lazy {
        S3RemoteDataSourceImpl(
            requireContext(),
            mainVm.userId
        )
    }

    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    val customId = GlobalApplication.prefs.getString("${loginType}_custom_id", "empty")

    private var isEmpty = false
    private val feedImageAdapter by lazy { FeedImageAdapter(this, customId) }

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
            feedImageAdapter.setFeedImages(it)
        })

        vm.isComplete.observe(viewLifecycleOwner, Observer {
            crossfade()
        })

        vm.deleteComplete.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "사진을 삭제했습니다.", Toast.LENGTH_SHORT).show()
        })

        mainVm.feedImages.observe(viewLifecycleOwner, Observer {
            feedImageAdapter.setFeedImages(it)
        })

        vm.behaviorSubject
            .observeOn(Schedulers.io())
            .subscribe { s3RemoteDataSource.deleteFileInS3("users/${userId}/feed/${it}") }
            .addTo(compositeDisposable)
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

    private fun crossfade() {
        binding.rvUserImage.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(500L)
                .setListener(null)
        }
    }

    private fun callDialog(imageName: String) {
        AlertDialog.Builder(requireContext()).setMessage(R.string.ask_remove_image_text)
            .setPositiveButton("예") { dialog, which ->
                vm.deleteFeedImage(imageName, userId!!)
            }
            .setNegativeButton("아니오") { dialog, which ->

            }.create().show()
    }

    override fun onLongClick(imageName: String) {
        callDialog(imageName)
    }

    override fun onClickItem(images: List<FeedImage>, position: Int) {
        (activity as MainActivity).replaceFragmentUseTagBackStack(
            DetailFragment(images, position),
            mainVm.tagName.value!!
        )
    }

    override fun onDestroy() {
        vm.unBindDisposable()
        compositeDisposable.clear()
        super.onDestroy()
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
