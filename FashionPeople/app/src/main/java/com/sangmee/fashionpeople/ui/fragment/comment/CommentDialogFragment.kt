package com.sangmee.fashionpeople.ui.fragment.comment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.CommentRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.data.repository.CommentRepositoryImpl
import com.sangmee.fashionpeople.data.repository.FeedImageRepositoryImpl
import com.sangmee.fashionpeople.databinding.FragmentCommentBinding
import com.sangmee.fashionpeople.ui.fragment.home.evaluate.EvaluateViewModel

class CommentDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCommentBinding
    private lateinit var myId: String

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
        val loginType = GlobalApplication.prefs.getString("login_type", "empty")
        myId = GlobalApplication.prefs.getString("${loginType}_custom_id", "")
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
        arguments?.getString(IMAGE_NAME)?.let { imageName ->
            viewModel.imageNameSubject.onNext(imageName)
            binding.ivSend.setOnClickListener {
                binding.etCommentInput.text?.let {
                    Log.d("seunghwan", it.toString())
                    if (it.isEmpty()) {
                        Toast.makeText(requireContext(), "메시지를 입력하세요", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.updateFeedImageComment(
                            myId,
                            imageName,
                            Comment(it.toString(), myId)
                        )
                    }
                }
            }
        }

        binding.etCommentInput.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if(s.isNotEmpty()) {
                        binding.ivSend.setImageDrawable(context?.getDrawable(R.drawable.ic_send_red_24))
                    } else {
                        binding.ivSend.setImageDrawable(context?.getDrawable(R.drawable.ic_send_gray_24))
                    }
                }
            }
        })

    }


    private fun initObserve() {
        viewModel.comments.observe(viewLifecycleOwner, Observer {
            commentRecyclerView.setComments(it)
            Log.d("seunghwan", "bind items = ${it}")
        })

        viewModel.feedImage.observe(viewLifecycleOwner, Observer {
            binding
        })

        viewModel.submitEvent.observe(viewLifecycleOwner, Observer {
            binding.etCommentInput.setText("")
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
