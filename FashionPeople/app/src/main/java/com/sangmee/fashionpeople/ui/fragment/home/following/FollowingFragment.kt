package com.sangmee.fashionpeople.ui.fragment.home.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.FragmentFollowingBinding
import com.sangmee.fashionpeople.observer.HomeViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.comment.CommentDialogFragment
import com.sangmee.fashionpeople.ui.fragment.grade.GradeDialogFragment
import com.sangmee.fashionpeople.ui.fragment.info.other.OtherFragment

class FollowingFragment : Fragment(), FollowingFeedAdapter.OnClickListener {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var followingFeedAdapter: FollowingFeedAdapter
    private val vm: HomeViewModel by activityViewModels()


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
        vm.getFollowingImages()
        initViewPager()
        initObserve()
    }

    private fun initViewPager() {
        followingFeedAdapter = FollowingFeedAdapter(vm.userId, { key ->
            val map = vm.followingFeedImagesIsSaved.value
            map?.let {
                it[key] = true
            }
            vm.followingFeedImagesIsSaved.value = map
        }, { key ->
            val map = vm.followingFeedImagesIsSaved.value
            map?.let {
                it[key] = false
            }
            vm.followingFeedImagesIsSaved.value = map
        })
        followingFeedAdapter.onClickListener = this@FollowingFragment
        binding.vpFollowing.apply {
            adapter = followingFeedAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL
        }
    }

    private fun initObserve() {
        vm.followingFeedImages.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                binding.vpFollowing.visibility = View.VISIBLE
                binding.tvEmptyResult.visibility = View.GONE
                followingFeedAdapter.setFeedImages(it)
            } else {
                binding.vpFollowing.visibility = View.GONE
                binding.tvEmptyResult.visibility = View.VISIBLE
            }
        })

        vm.savedFeedImages.observe(viewLifecycleOwner, Observer { saveImages ->
            vm.followingFeedImages.value?.let { feedImages ->
                val map = mutableMapOf<String, Boolean>()
                for (feedImage in feedImages) {
                    var isExist = false
                    for (saveImage in saveImages) {
                        if (feedImage.imageName == saveImage.imageName) {
                            isExist = true
                        }
                    }
                    map[feedImage.imageName!!] = isExist
                }
                vm.followingFeedImagesIsSaved.value = map
            }
        })

        vm.followingFeedImagesIsSaved.observe(viewLifecycleOwner, Observer {
            vm.followingLoadingComplete.value = true
            followingFeedAdapter.setSavedButtonType(it)
        })

        vm.updateFeedImages.observe(viewLifecycleOwner, Observer {
            it?.let {
                followingFeedAdapter.updateItem(it)
            }
        })

        vm.followingLoadingComplete.observe(viewLifecycleOwner, Observer {

            if (it) {
                binding.pbLoading.isVisible = false
                binding.vpFollowing.isVisible = true
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

    override fun onDestroy() {
        vm.clearDisposable()
        super.onDestroy()
    }

    override fun onClickRatingBar(
        ratingBar: RatingBar?,
        rating: Float,
        fromUser: Boolean,
        feedImage: FeedImage
    ) {
        ratingBar?.rating = rating
        feedImage.imageName?.let { vm.ratingClick(it, rating) }
    }

    override fun onClickComment(imageName: String) {
        showCommentFragment(imageName)
    }

    override fun onClickGrade(feedImage: FeedImage) {
        showGradeFragment(feedImage)
    }

    override fun onClickSave(imageName: String) {
        vm.postSaveImage(imageName)
    }

    override fun onClickDelete(imageName: String) {
        vm.deleteSaveImage(imageName)
    }

    override fun onClickProfile(feedImage: FeedImage) {
        feedImage.user?.id?.let { OtherFragment.newInstance(it) }?.let {
            (activity as MainActivity).replaceFragmentUseBackStack(it)
        }
    }


}
