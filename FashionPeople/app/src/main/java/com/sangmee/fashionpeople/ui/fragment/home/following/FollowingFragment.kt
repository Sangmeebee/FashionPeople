package com.sangmee.fashionpeople.ui.fragment.home.following

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.repository.FeedImageRepositoryImpl
import com.sangmee.fashionpeople.databinding.DialogBaseBinding
import com.sangmee.fashionpeople.databinding.FragmentFollowingBinding
import com.sangmee.fashionpeople.ui.fragment.comment.CommentDialogFragment
import com.sangmee.fashionpeople.ui.fragment.grade.GradeDialogFragment
import com.sangmee.fashionpeople.ui.fragment.home.evaluate.EvaluateFeedAdapter
import com.sangmee.fashionpeople.ui.fragment.home.evaluate.EvaluateViewModel

class FollowingFragment : Fragment(), EvaluateFeedAdapter.OnClickListener,
    FollowingFeedAdapter.OnClickListener {

    private lateinit var binding: FragmentFollowingBinding
    private val pref = GlobalApplication.prefs
    lateinit var customId: String

    private lateinit var followingFeedAdapter: FollowingFeedAdapter


    private val viewModel: FollowingViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return FollowingViewModel(FeedImageRepositoryImpl(FeedImageRemoteDataSourceImpl())) as T
            }
        }).get(FollowingViewModel::class.java)
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
        setId()
        initViewPager()
        initObserve()
    }

    private fun initObserve() {
        viewModel.feedImages.observe(this@FollowingFragment, Observer {
            it?.let {
                followingFeedAdapter.setFeedImages(it)
            }
        })

        viewModel.updateFeedImages.observe(viewLifecycleOwner, Observer {
            it?.let {
                followingFeedAdapter.updateItem(it)
            }
        })

        viewModel.evaluateMessage.observe(viewLifecycleOwner, Observer {
            val binding = DataBindingUtil.inflate<DialogBaseBinding>(
                layoutInflater,
                R.layout.dialog_base,
                null,
                false
            )
            val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
            builder.setView(binding.root)

            val alertDialog = builder.create()
            val window = alertDialog.window
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()

            binding.tvDialogMessage.text = "평가가 완료되었습니다\n 사진을 저장하시겠습니까?"
            binding.btnOk.setOnClickListener {
                alertDialog.dismiss()
            }

            binding.btnCancel.setOnClickListener {
                alertDialog.dismiss()
            }
        })
    }

    private fun initViewPager() {
        followingFeedAdapter = FollowingFeedAdapter(myId = customId)
        followingFeedAdapter.onClickListener = this@FollowingFragment
        binding.vpFollowing.apply {
            adapter = followingFeedAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun setId() {
        customId = pref.getString("custom_id", "empty")
        viewModel.idSubject.onNext(customId)
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



}