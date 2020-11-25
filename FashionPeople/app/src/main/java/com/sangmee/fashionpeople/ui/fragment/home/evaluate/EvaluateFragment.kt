package com.sangmee.fashionpeople.ui.fragment.home.evaluate

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
import com.sangmee.fashionpeople.databinding.DialogBaseBinding
import com.sangmee.fashionpeople.databinding.FragmentEvaluateBinding
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.repository.FeedImageRepositoryImpl
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.comment.CommentDialogFragment
import com.sangmee.fashionpeople.ui.fragment.grade.GradeDialogFragment
import com.sangmee.fashionpeople.ui.fragment.info.other.OtherFragment

class EvaluateFragment : Fragment(), EvaluateFeedAdapter.OnClickListener {

    private lateinit var binding: FragmentEvaluateBinding
    private val pref = GlobalApplication.prefs
    lateinit var customId: String

    private val viewModel: EvaluateViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return EvaluateViewModel(
                    FeedImageRepositoryImpl(feedImageRemoteDataSource = FeedImageRemoteDataSourceImpl())
                ) as T
            }
        }).get(EvaluateViewModel::class.java)
    }

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
        customId = pref.getString("custom_id", "empty")
        viewModel.idSubject.onNext(customId)
    }

    private fun initViewPager() {
        evaluateFeedAdapter = EvaluateFeedAdapter(myId = customId)
        evaluateFeedAdapter.onClickListener = this@EvaluateFragment
        binding.vpEvaluate.apply {
            adapter = evaluateFeedAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
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
        (activity as MainActivity).replaceFragmentUseBackStack(
            OtherFragment.newInstance(
                customId,
                0
            )
        )
    }
}
