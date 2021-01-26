package com.sangmee.fashionpeople.ui.fragment.home.evaluate

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.FragmentEvaluateBinding
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.comment.CommentDialogFragment
import com.sangmee.fashionpeople.ui.fragment.grade.GradeDialogFragment
import com.sangmee.fashionpeople.ui.fragment.home.HomeViewModel
import com.sangmee.fashionpeople.ui.fragment.info.other.OtherFragment
import com.sangmee.fashionpeople.ui.fragment.tag.TagDialogFragment
import com.willy.ratingbar.BaseRatingBar

class EvaluateFragment : Fragment(), EvaluateFeedAdapter.OnClickListener {

    private lateinit var binding: FragmentEvaluateBinding
    private val vm by activityViewModels<HomeEvaluateViewModel>()
    private val homeVm by activityViewModels<HomeViewModel>()
    private val mainVm by activityViewModels<MainViewModel>()
    private lateinit var evaluateFeedAdapter: EvaluateFeedAdapter
    private var pos: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getOtherImages()
        evaluateFeedAdapter = EvaluateFeedAdapter()
    }

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
        initViewPager()
        initIsExist()
        initObserve()
    }

    private fun initViewPager() {
        evaluateFeedAdapter.onClickListener = this@EvaluateFragment
        binding.vpEvaluate.apply {
            adapter = evaluateFeedAdapter
            offscreenPageLimit = 50
            orientation = ViewPager2.ORIENTATION_VERTICAL
        }
    }

    private fun initObserve() {
        vm.evaluateFeedImages.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                binding.vpEvaluate.visibility = View.VISIBLE
                binding.tvEmptyResult.visibility = View.GONE
                //평가 이미지 리사이클러뷰 세팅
                evaluateFeedAdapter.setFeedImages(it)
                //저장 버튼 리사이클러뷰 세팅
                val saveImages = mutableListOf<String>()
                mainVm.saveImages.value?.let { feedImages ->
                    for (feedImage in feedImages) {
                        saveImages.add(feedImage.imageName!!)
                    }
                    evaluateFeedAdapter.setSaveItems(saveImages, null)
                }
            } else {
                binding.vpEvaluate.visibility = View.GONE
                binding.tvEmptyResult.visibility = View.VISIBLE
            }
        })

        vm.updateFeedImage.observe(viewLifecycleOwner, Observer {
            it?.let {
                evaluateFeedAdapter.updateItem(it)
            }
        })

        vm.evaluateLoadingComplete.observe(viewLifecycleOwner, Observer {
            homeVm.evaluatedIsAdded.value = true
            crossfade()
        })

        vm.saveComplete.observe(this, Observer {
            Toast.makeText(context, "사진을 저장했습니다.", Toast.LENGTH_SHORT).show()
            mainVm.getMySaveImage()
        })

        vm.deleteComplete.observe(this, Observer {
            Toast.makeText(context, "사진을 삭제했습니다.", Toast.LENGTH_SHORT).show()
            mainVm.getMySaveImage()
        })

        vm.errorComplete.observe(this, Observer {
            Toast.makeText(context, "이미 평가 완료된 사진", Toast.LENGTH_SHORT).show()
        })

        mainVm.saveImages.observe(viewLifecycleOwner, Observer {
            val saveImages = mutableListOf<String>()
            for (feedImage in it) {
                saveImages.add(feedImage.imageName!!)
            }

            evaluateFeedAdapter.setSaveItems(saveImages, pos)
        })
    }

    private fun initIsExist() {
        homeVm.evaluatedIsAdded.value?.let {
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
        vm.postSaveImage(imageName)
        pos = position
    }

    override fun onClickDelete(imageName: String, position: Int) {
        vm.deleteSaveImage(imageName)
        pos = position
    }

    override fun onClickProfile(feedImage: FeedImage) {
        feedImage.user?.id?.let { OtherFragment.newInstance(it) }?.let {
            (activity as MainActivity).replaceFragmentUseBackStack(it)
        }
    }

    override fun onClickRatingBar(
        ratingBar: BaseRatingBar?,
        rating: Float,
        fromUser: Boolean,
        feedImage: FeedImage
    ) {
        feedImage.imageName?.let { vm.ratingClick(it, rating) }
    }

    override fun onClickTag(feedImage: FeedImage) {
        showTagFragment(feedImage)
    }
}
