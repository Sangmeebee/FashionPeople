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
import com.sangmee.fashionpeople.data.model.Evaluation
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.FragmentGradeBinding


class GradeDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentGradeBinding
    private lateinit var evaluationList: List<Evaluation>

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
        arguments?.getParcelable<FeedImage>(IMAGE_NAME)?.let { feedImage ->
            binding.feedImage = feedImage
            feedImage.evaluations?.let {
                evaluationList = it
            }
        }
        setProgressView()
    }

    private fun setProgressView() {
        val total = evaluationList.size

        var progressValue1 = 0
        var progressValue2 = 0
        var progressValue3 = 0
        var progressValue4 = 0
        var progressValue5 = 0

        if (evaluationList.isNotEmpty()) {
            for (i in evaluationList.indices) {
                when (evaluationList[i].score) {
                    1f -> {
                        progressValue1++
                    }
                    2f -> {
                        progressValue2++
                    }
                    3f -> {
                        progressValue3++
                    }
                    4f -> {
                        progressValue4++
                    }
                    5f -> {
                        progressValue5++
                    }
                }
            }
            binding.progress1.progress = (progressValue1 / total * 100).toFloat() + 5
            binding.progress1.labelText = "${progressValue1}명"
            binding.progress2.progress = (progressValue2 / total * 100).toFloat() + 5
            binding.progress2.labelText = "${progressValue2}명"
            binding.progress3.progress = (progressValue3 / total * 100).toFloat() + 5
            binding.progress3.labelText = "${progressValue3}명"
            binding.progress4.progress = (progressValue4 / total * 100).toFloat() + 5
            binding.progress4.labelText = "${progressValue4}명"
            binding.progress5.progress = (progressValue5 / total * 100).toFloat() + 5
            binding.progress5.labelText = "${progressValue5}명"
        } else {
            binding.progress1.progress = 5f
            binding.progress1.labelText = "0명"
            binding.progress2.progress = 5f
            binding.progress2.labelText = "0명"
            binding.progress3.progress = 5f
            binding.progress3.labelText = "0명"
            binding.progress4.progress = 5f
            binding.progress4.labelText = "0명"
            binding.progress5.progress = 5f
            binding.progress5.labelText = "0명"
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