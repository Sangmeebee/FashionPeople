package com.sangmee.fashionpeople.ui.fragment.home.evaluate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.FragmentEvaluateBinding
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.comment.CommentDialogFragment
import com.sangmee.fashionpeople.ui.fragment.grade.GradeDialogFragment
import com.sangmee.fashionpeople.ui.fragment.info.other.OtherFragment

class EvaluateFragment : Fragment(), EvaluateFeedAdapter.OnClickListener {

    private lateinit var binding: FragmentEvaluateBinding
    private val pref = GlobalApplication.prefs
    lateinit var customId: String

    private val viewModel: EvaluateViewModel by activityViewModels()

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
        setId()
        initViewPager()
        initObserve()
    }

    private fun setId() {
        val loginType = GlobalApplication.prefs.getString("login_type", "empty")
        customId = pref.getString("${loginType}_custom_id", "empty")
        viewModel.idSubject.onNext(customId)
    }

    private fun initViewPager() {
        evaluateFeedAdapter = EvaluateFeedAdapter(myId = customId)
        evaluateFeedAdapter.onClickListener = this@EvaluateFragment
        binding.vpEvaluate.apply {
            adapter = evaluateFeedAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL
        }
    }

    private fun initObserve() {
        viewModel.feedImages.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                binding.vpEvaluate.visibility = View.VISIBLE
                binding.tvEmptyResult.visibility = View.GONE
                evaluateFeedAdapter.setFeedImages(it)
            } else {
                binding.vpEvaluate.visibility = View.GONE
                binding.tvEmptyResult.visibility = View.VISIBLE
            }
        })

        viewModel.updateFeedImages.observe(viewLifecycleOwner, Observer {
            it?.let {
                evaluateFeedAdapter.updateItem(it)
            }
        })

        viewModel.evaluateMessage.observe(viewLifecycleOwner, Observer {
            AlertDialog.Builder(requireContext()).setMessage(R.string.complete_evaluation_text)
                .setPositiveButton("네") { dialog, which ->
                }
                .setNegativeButton("아니오") { dialog, which ->

                }.create().show()
        })


        viewModel.isComplete.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.pbLoading.isVisible = false
                binding.vpEvaluate.isVisible = true
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
        viewModel.clearDisposable()
        super.onDestroy()
    }

    override fun onClickRatingBar(
        ratingBar: RatingBar?,
        rating: Float,
        fromUser: Boolean,
        feedImage: FeedImage
    ) {
        ratingBar?.rating = rating
        feedImage.imageName?.let { viewModel.ratingClick(it, rating) }
    }

    override fun onClickComment(imageName: String) {
        showCommentFragment(imageName)
    }

    override fun onClickGrade(feedImage: FeedImage) {
        showGradeFragment(feedImage)
    }

    override fun onClickProfile(feedImage: FeedImage) {
        feedImage.user?.id?.let { OtherFragment.newInstance(it) }?.let {
            (activity as MainActivity).replaceFragmentUseBackStack(it)
        }
    }
}
