package com.sangmee.fashionpeople.ui.fragment.comment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.dataSource.remote.CommentRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.repository.CommentRepositoryImpl
import com.sangmee.fashionpeople.data.repository.FeedImageRepositoryImpl
import com.sangmee.fashionpeople.databinding.FragmentCommentBinding
import com.sangmee.fashionpeople.ui.fragment.home.evaluate.EvaluateViewModel

class CommentDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCommentBinding

    private val commentRecyclerView: CommentRecyclerAdapter by lazy {
        CommentRecyclerAdapter()
    }

    private val viewModel: CommentViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CommentViewModel(
                    commentRepository = CommentRepositoryImpl(CommentRemoteDataSourceImpl()),
                    feedImageRepository = FeedImageRepositoryImpl(FeedImageRemoteDataSourceImpl())
                ) as T
            }
        }).get(CommentViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        dialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initView()
        initObserve()
    }

    private fun initRecyclerView() {
        binding.rvComment.adapter = commentRecyclerView
    }

    private fun initView() {
        arguments?.getString(IMAGE_NAME)?.let {
            viewModel.imageNameSubject.onNext(it)
        }

    }


    private fun initObserve() {
        viewModel.comments.observe(viewLifecycleOwner, Observer {
            commentRecyclerView.setComments(it)
            Log.d("seunghwan", "bind items = ${it}")
        })

        viewModel.feedImage.observe(viewLifecycleOwner, Observer {
            binding
        })
    }

    override fun onDestroy() {
        viewModel.clearDisposable()
        super.onDestroy()
    }

    companion object {
        val TAG = this::class.java.simpleName

        private const val IMAGE_NAME = "image_name"

        fun newInstance(imageName: String) = CommentDialogFragment().apply {
            arguments = Bundle().apply {
                putString(IMAGE_NAME, imageName)
            }
            setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetStyle)
        }
    }
}