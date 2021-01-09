package com.sangmee.fashionpeople.ui.fragment.home.evaluate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.FragmentEvaluateBinding
import com.sangmee.fashionpeople.observer.HomeViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.comment.CommentDialogFragment
import com.sangmee.fashionpeople.ui.fragment.grade.GradeDialogFragment
import com.sangmee.fashionpeople.ui.fragment.info.other.OtherFragment

class EvaluateFragment : Fragment(), EvaluateFeedAdapter.OnClickListener {

    private lateinit var binding: FragmentEvaluateBinding
    private val vm: HomeViewModel by activityViewModels()
    private lateinit var evaluateFeedAdapter: EvaluateFeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_evaluate, container, false)
        binding.lifecycleOwner = this@EvaluateFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.getOtherImages()
        initViewPager()
        initObserve()
    }

    private fun initViewPager() {
        evaluateFeedAdapter = EvaluateFeedAdapter(vm.userId) { key, isSaved ->
            val eMap = vm.evaluateFeedImagesIsSaved.value
            val fMap = vm.evaluateFeedImagesIsSaved.value
            eMap?.let {
                it[key] = isSaved
            }
            fMap?.let {
                it[key] = isSaved
            }
            vm.evaluateFeedImagesIsSaved.value = eMap
            vm.followingFeedImagesIsSaved.value = fMap
        }
        evaluateFeedAdapter.onClickListener = this@EvaluateFragment
        binding.vpEvaluate.apply {
            adapter = evaluateFeedAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL
        }
    }

    private fun initObserve() {
        vm.evaluateFeedImages.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                binding.vpEvaluate.visibility = View.VISIBLE
                binding.tvEmptyResult.visibility = View.GONE
                evaluateFeedAdapter.setFeedImages(it)
            } else {
                binding.vpEvaluate.visibility = View.GONE
                binding.tvEmptyResult.visibility = View.VISIBLE
            }
        })

        vm.savedFeedImages.observe(viewLifecycleOwner, Observer { saveImages ->
            vm.evaluateFeedImages.value?.let { feedImages ->
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
                vm.evaluateFeedImagesIsSaved.value = map
            }
        })
        vm.evaluateFeedImagesIsSaved.observe(viewLifecycleOwner, Observer {
            vm.evaluateLoadingComplete.value = true
            evaluateFeedAdapter.setSavedButtonType(it)
        })

        vm.updateFeedImage.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("Sangmeebee", "evaluate_${it}")
                evaluateFeedAdapter.updateItem(it)
            }
        })

        vm.evaluateLoadingComplete.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.pbLoading.isVisible = false
                binding.clContainer.isVisible = true
            }
        })

        vm.saveComplete.observe(this, Observer {
            if (it) {
                Toast.makeText(context, "사진을 저장했습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        vm.deleteComplete.observe(this, Observer {
            if (it) {
                Toast.makeText(context, "사진을 삭제했습니다.", Toast.LENGTH_SHORT).show()
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
