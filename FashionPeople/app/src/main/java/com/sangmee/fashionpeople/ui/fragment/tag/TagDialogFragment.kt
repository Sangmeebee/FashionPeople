package com.sangmee.fashionpeople.ui.fragment.tag

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.FragmentDialogTagBinding


class TagDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogTagBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_tag, container, false)
        binding.lifecycleOwner = this
        dialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Sangmeebee", TAG)
        arguments?.getParcelable<FeedImage>(IMAGE_NAME)?.let { feedImage ->
            binding.feedImage = feedImage
        }
    }

    companion object {

        val TAG = this::class.java.simpleName

        private const val IMAGE_NAME = "image_name"

        fun newInstance(feedImage: FeedImage) = TagDialogFragment().apply {
            arguments = Bundle().apply {
                putParcelable(IMAGE_NAME, feedImage)
            }
            setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetStyle)
        }
    }
}
