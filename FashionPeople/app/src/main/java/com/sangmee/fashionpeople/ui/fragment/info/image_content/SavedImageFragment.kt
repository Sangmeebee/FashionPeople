package com.sangmee.fashionpeople.ui.fragment.info.image_content

import android.graphics.Point
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.FragmentSavedImageBinding
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.observer.SavedImageViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.detail.DetailFragment

class SavedImageFragment : Fragment(), SaveImageAdapter.OnClickListener {

    private var userId: String? = null
    private lateinit var binding: FragmentSavedImageBinding
    private val vm: SavedImageViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SavedImageViewModel() as T
            }
        }).get(SavedImageViewModel::class.java)
    }
    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    val customId = GlobalApplication.prefs.getString("${loginType}_custom_id", "empty")

    private val mainVm by activityViewModels<MainViewModel>()

    private var isEmpty = false
    private val saveImageAdapter by lazy { SaveImageAdapter(this) }

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
        return inflater.inflate(R.layout.fragment_saved_image, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId?.let {
            if (it == customId) {
                mainVm.getMySaveImage()
            } else {
                vm.callSavedImages(it)
            }
        }
        setRecyclerView()
    }

    private fun setRecyclerView() {
        val height = getDisplayHeight()
        binding.rvUserImage.apply {
            setHasFixedSize(true)
            minimumHeight = height
            adapter = saveImageAdapter
        }
    }

    private fun viewModelCallback() {
        vm.savedImages.observe(viewLifecycleOwner, Observer {
            saveImageAdapter.setFeedImages(it)
        })

        vm.isComplete.observe(viewLifecycleOwner, Observer {
            crossfade()
        })

        mainVm.saveImages.observe(viewLifecycleOwner, Observer {
            saveImageAdapter.setFeedImages(it)
        })

        mainVm.callSaveImageComplete.observe(viewLifecycleOwner, Observer {
            crossfade()
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


    override fun onDestroy() {
        vm.unBindDisposable()
        super.onDestroy()
    }

    override fun onClickItem(images: List<FeedImage>, position: Int) {
        (activity as MainActivity).replaceFragmentUseTagBackStack(
            DetailFragment(images, position),
            mainVm.tagName.value!!
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(userId: String) =
            SavedImageFragment().apply {
                arguments = Bundle().apply {
                    putString("custom_id", userId)
                }
            }
    }
}
