package com.sangmee.fashionpeople.ui.fragment.grade

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.FragmentGradeBinding


class GradeDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentGradeBinding

    private val viewModel: GradeViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return GradeViewModel() as T
            }
        }).get(GradeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_grade, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        dialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<FeedImage>(IMAGE_NAME)?.let {
            binding.feedImage = it
        }

    }

    companion object {

        val TAG = this::class.java.simpleName

        private const val IMAGE_NAME = "image_name"

        fun newInstance(feedImage: FeedImage) = GradeDialogFragment().apply {
            arguments = Bundle().apply {
                putParcelable(IMAGE_NAME, feedImage)
            }
            setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetStyle)
        }
    }
}