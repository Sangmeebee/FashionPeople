package com.sangmee.fashionpeople.ui.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.comment.CommentDialogFragment
import com.sangmee.fashionpeople.ui.fragment.grade.GradeDialogFragment
import com.sangmee.fashionpeople.ui.fragment.info.other.OtherFragment
import com.sangmee.fashionpeople.ui.fragment.tag.TagDialogFragment
import com.sangmee.fashionpeople.ui.login.LoginDialogFragment
import com.willy.ratingbar.BaseRatingBar
import kotlinx.android.synthetic.main.fragment_search_detail.*

class DetailFragment(private val feedImages: List<FeedImage>, private val position: Int) :
    Fragment(), DetailAdapter.OnClickListener {

    private val viewModel: DetailViewModel by viewModels()
    private val mainVm by activityViewModels<MainViewModel>()
    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    val userId = GlobalApplication.prefs.getString("${loginType}_custom_id", "empty")
    private var pos: Int? = null

    private lateinit var detailAdapter: DetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.isAdded.value = false
        detailAdapter = DetailAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCommentNum()
        initViewPager()
        initView()
        initObserve()
    }

    private fun initCommentNum() {
        val map = mainVm.comments.value!!
        for (feedImage in feedImages) {
            feedImage.comments?.size?.let { map.put(feedImage.imageName!!, it) }
        }
        mainVm.comments.value = map
    }

    private fun initViewPager() {
        detailAdapter.onClickListener = this@DetailFragment
        vp_detail.apply {
            adapter = detailAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL
            post {
                viewModel.isAdded.value?.let {
                    if (!it) {
                        setCurrentItem(position, false)
                        viewModel.isAdded.value = true
                    } else {
                        setCurrentItem(viewModel.currentIndex.value!!, false)
                    }

                }
                crossfade()
            }
        }

        vp_detail.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.currentIndex.value = position
            }
        })
    }

    private fun initView() {
        detailAdapter.setFeedImages(feedImages)
        val saveImages = mutableListOf<String>()
        mainVm.saveImages.value?.let {
            for (feedImage in it) {
                saveImages.add(feedImage.imageName!!)
            }
            detailAdapter.setSaveItems(saveImages, null)
        }
    }

    private fun initObserve() {

        mainVm.saveImages.observe(viewLifecycleOwner, Observer {
            val saveImages = mutableListOf<String>()
            for (feedImage in it) {
                saveImages.add(feedImage.imageName!!)
            }

            detailAdapter.setSaveItems(saveImages, pos)
        })

        mainVm.updateFeedImage.observe(viewLifecycleOwner, Observer {
            detailAdapter.updateItem(it)
        })

        mainVm.comments.observe(viewLifecycleOwner, Observer {
            detailAdapter.setCommentNum(it)
        })
    }

    private fun showCommentFragment(imageName: String) {
        CommentDialogFragment.newInstance(imageName)
            .show(childFragmentManager, CommentDialogFragment.TAG)
    }

    private fun showGradeFragment(feedImage: FeedImage) {
        GradeDialogFragment.newInstance(feedImage)
            .show(childFragmentManager, GradeDialogFragment.TAG)
    }

    private fun showTagFragment(feedImage: FeedImage) {
        TagDialogFragment.newInstance(feedImage)
            .show(childFragmentManager, TagDialogFragment.TAG)
    }

    override fun onClickComment(imageName: String) {
        if (mainVm.userId == "empty") {
            LoginDialogFragment().show(parentFragmentManager, "LoginDialog")
        } else {
            showCommentFragment(imageName)
        }
    }

    override fun onClickGrade(feedImage: FeedImage) {
        showGradeFragment(feedImage)
    }

    override fun onClickProfile(feedImage: FeedImage) {
        feedImage.user?.id?.let { OtherFragment.newInstance(it) }?.let {
            (activity as MainActivity).replaceFragmentUseTagBackStack(it, mainVm.tagName.value!!)
        }
    }

    override fun onClickSave(imageName: String, position: Int) {
        if (mainVm.userId == "empty") {
            LoginDialogFragment().show(parentFragmentManager, "LoginDialog")
        } else {
            mainVm.postSaveImage(imageName)
            pos = position
        }
    }

    override fun onClickDelete(imageName: String, position: Int) {
        mainVm.deleteSaveImage(imageName)
        pos = position
    }

    override fun onClickTag(feedImage: FeedImage) {
        showTagFragment(feedImage)
    }

    override fun onClickRatingBar(
        ratingBar: BaseRatingBar?,
        rating: Float,
        fromUser: Boolean,
        feedImage: FeedImage
    ) {
        if (mainVm.userId == "empty") {
            ratingBar?.setFilledDrawableRes(R.drawable.star)
            LoginDialogFragment().show(parentFragmentManager, "LoginDialog")
        } else {
            feedImage.imageName?.let { mainVm.ratingClick(it, rating) }
        }
    }

    private fun crossfade() {
        vp_detail.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(500L)
                .setListener(null)
        }
    }
}
