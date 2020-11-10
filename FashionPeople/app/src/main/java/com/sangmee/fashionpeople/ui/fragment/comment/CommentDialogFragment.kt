package com.sangmee.fashionpeople.ui.fragment.comment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentCommentBinding
import com.sangmee.fashionpeople.ui.fragment.home.evaluate.EvaluateViewModel

class CommentDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCommentBinding

    private val viewModel: CommentViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CommentViewModel() as T
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

}