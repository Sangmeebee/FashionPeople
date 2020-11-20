package com.sangmee.fashionpeople.ui.fragment.comment

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentInputCommentBinding

class FragmentInputComment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentInputCommentBinding

    private lateinit var nowText: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_input_comment, container, false)
        binding.lifecycleOwner = this
        dialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(NOW_TEXT)?.let {
            nowText = it
            binding.etCommentInput.setText(it)
        }

        binding.etCommentInput.addTextChangedListener(object : TextWatcher {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    binding.ivSend.setImageDrawable(context?.getDrawable(R.drawable.ic_send_red_24))
                } else {
                    binding.ivSend.setImageDrawable(context?.getDrawable(R.drawable.ic_send_gray_24))
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.ivSend.setOnClickListener {
            if(binding.etCommentInput.text?.length!! > 0) {

            }
        }
    }


    companion object {
        val TAG = this::class.java.simpleName

        private const val NOW_TEXT = "now_text"

        fun newInstance(nowText: String) = FragmentInputComment().apply {
            arguments = Bundle().apply {
                putString(NOW_TEXT, nowText)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

}