package com.sangmee.fashionpeople.ui.fragment.home.following

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.FragmentFollowingBinding
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.comment.CommentDialogFragment
import com.sangmee.fashionpeople.ui.fragment.grade.GradeDialogFragment
import com.sangmee.fashionpeople.ui.fragment.home.HomeViewModel
import com.sangmee.fashionpeople.ui.fragment.home.evaluate.HomeEvaluateViewModel
import com.sangmee.fashionpeople.ui.fragment.info.other.OtherFragment
import com.sangmee.fashionpeople.ui.fragment.tag.TagDialogFragment
import com.willy.ratingbar.BaseRatingBar

class FollowingFragment : Fragment(), FollowingFeedAdapter.OnClickListener {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var followingFeedAdapter: FollowingFeedAdapter
    private val vm by activityViewModels<HomeEvaluateViewModel>()
    private val homeVm by activityViewModels<HomeViewModel>()
    private val mainVm by activityViewModels<MainViewModel>()
    private var pos: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getFollowingImages()
        followingFeedAdapter = FollowingFeedAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_following, container, false)
        binding.lifecycleOwner = this@FollowingFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initIsExist()
        initObserve()
    }

    private fun initViewPager() {
        followingFeedAdapter.onClickListener = this@FollowingFragment
        binding.vpFollowing.apply {
            adapter = followingFeedAdapter
            offscreenPageLimit = 50
            orientation = ViewPager2.ORIENTATION_VERTICAL
        }
    }

    private fun initObserve() {
        vm.followingFeedImages.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                binding.vpFollowing.visibility = View.VISIBLE
                binding.tvEmptyResult.visibility = View.GONE
                followingFeedAdapter.setFeedImages(it)
                //저장 버튼 리사이클러뷰 세팅
                val saveImages = mutableListOf<String>()
                mainVm.saveImages.value?.let { feedImages ->
                    for (feedImage in feedImages) {
                        saveImages.add(feedImage.imageName!!)
                    }
                    followingFeedAdapter.setSaveItems(saveImages, null)
                }
            } else {
                binding.vpFollowing.visibility = View.GONE
                binding.tvEmptyResult.visibility = View.VISIBLE
            }
        })

        mainVm.updateFeedImage.observe(viewLifecycleOwner, Observer {
            Log.d("SangmeebeeFollowing", it.toString())
            it?.let {
                followingFeedAdapter.updateItem(it)
            }
        })

        vm.followingLoadingComplete.observe(viewLifecycleOwner, Observer {
            homeVm.followingIsAdded.value = true
            crossfade()
        })

        mainVm.saveImages.observe(viewLifecycleOwner, Observer {
            val saveImages = mutableListOf<String>()
            for (feedImage in it) {
                saveImages.add(feedImage.imageName!!)
            }

            followingFeedAdapter.setSaveItems(saveImages, pos)
        })
    }

    private fun initIsExist() {
        homeVm.followingIsAdded.value?.let {
            if(it){
                binding.clContainer.visibility = View.VISIBLE
                binding.pbLoading.visibility = View.GONE
            }
        }
    }

    private fun crossfade() {
        binding.clContainer.apply {
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

    override fun onPause() {
        vm.clearDisposable()
        super.onPause()
    }

    override fun onClickComment(imageName: String) {
        showCommentFragment(imageName)
    }

    override fun onClickGrade(feedImage: FeedImage) {
        showGradeFragment(feedImage)
    }

    override fun onClickSave(imageName: String, position: Int) {
        mainVm.postSaveImage(imageName)
        pos = position
    }

    override fun onClickDelete(imageName: String, position: Int) {
        mainVm.deleteSaveImage(imageName)
        pos = position
    }

    override fun onClickProfile(feedImage: FeedImage) {
        feedImage.user?.id?.let { OtherFragment.newInstance(it) }?.let {
            (activity as MainActivity).replaceFragmentUseTagBackStack(it, mainVm.tagName.value!!)
        }
    }

    override fun onClickRatingBar(
        ratingBar: BaseRatingBar?,
        rating: Float,
        fromUser: Boolean,
        feedImage: FeedImage
    ) {
        feedImage.imageName?.let { mainVm.ratingClick(it, rating) }
    }

    override fun onClickTag(feedImage: FeedImage) {
        showTagFragment(feedImage)
    }
}
