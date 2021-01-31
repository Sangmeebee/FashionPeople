package com.sangmee.fashionpeople.ui.fragment.comment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.CommentRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.data.repository.CommentRepositoryImpl
import com.sangmee.fashionpeople.data.repository.FeedImageRepositoryImpl
import com.sangmee.fashionpeople.databinding.FragmentCommentBinding
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.info.other.OtherFragment

private const val IMAGE_NAME = "image_name"

class CommentDialogFragment : BottomSheetDialogFragment(), CommentRecyclerAdapter.OnClickListener {

    private lateinit var binding: FragmentCommentBinding
    private lateinit var myId: String
    private var imageName: String? = null
    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    val customId = GlobalApplication.prefs.getString("${loginType}_custom_id", "empty")

    private val commentRecyclerView: CommentRecyclerAdapter by lazy {
        CommentRecyclerAdapter(this, customId)
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
    private val mainVm by activityViewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageName = it.getString(IMAGE_NAME)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.behavior.skipCollapsed = true
            dialog.behavior.state = STATE_EXPANDED
        }
        return dialog
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
        myId = customId
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initView()
        initObserve()
    }

    private fun initRecyclerView() {
        binding.rvComment.apply {
            adapter = commentRecyclerView
            setHasFixedSize(true)
        }
    }

    private fun initView() {
        imageName?.let { imageName ->
            viewModel.imageNameSubject.onNext(imageName)
            binding.ivSend.setOnClickListener {
                binding.etCommentInput.text?.let {
                    if (it.isEmpty()) {
                        Toast.makeText(requireContext(), "메시지를 입력하세요", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.updateFeedImageComment(
                            myId,
                            imageName,
                            Comment(it.toString())
                        )
                    }
                }
            }
        }


        binding.etCommentInput.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.isNotEmpty()) {
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
        })


        viewModel.submitEvent.observe(viewLifecycleOwner, Observer {
            binding.etCommentInput.setText("")
        })

        viewModel.deleteComplete.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "댓글을 삭제했습니다.", Toast.LENGTH_SHORT).show()
        })
    }

    private fun callDialog(id: Int) {
        AlertDialog.Builder(requireContext()).setMessage(R.string.ask_remove_comment_text)
            .setPositiveButton("예") { dialog, which ->
                viewModel.deleteComment(id, imageName!!)
            }
            .setNegativeButton("아니오") { dialog, which ->

            }.create().show()
    }

    override fun longClick(id: Int) {
        callDialog(id)
    }

    override fun clickProfile(id: String) {
        OtherFragment.newInstance(id).let {
            (activity as MainActivity).replaceFragmentUseTagBackStack(it, mainVm.tagName.value!!)
        }
    }

    override fun onDestroy() {
        viewModel.clearDisposable()
        super.onDestroy()
    }

    companion object {
        val TAG = this::class.java.simpleName

        fun newInstance(imageName: String) = CommentDialogFragment().apply {
            arguments = Bundle().apply {
                putString(IMAGE_NAME, imageName)
            }
            setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetStyle)
        }
    }
}
