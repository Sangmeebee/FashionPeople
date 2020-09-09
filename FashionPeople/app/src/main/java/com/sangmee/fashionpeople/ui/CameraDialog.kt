package com.sangmee.fashionpeople.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.sangmee.fashionpeople.R

class CameraDialog : DialogFragment() {

    var onCameraClick: (() -> Unit)? = null
    var onVideoClick: (()-> Unit)? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return inflater.inflate(R.layout.camera_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.iv_camera).setOnClickListener {
            onCameraClick?.invoke()
        }

        view.findViewById<ImageView>(R.id.iv_video).setOnClickListener {
            onVideoClick?.invoke()
        }

    }




    override fun onResume() {
        super.onResume()

        //다이얼로그 크기 조정
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val width = (metrics.widthPixels * 0.7).toInt()
        val height = (metrics.heightPixels * 0.5).toInt()
        dialog?.window?.setLayout(width, height)
    }

    companion object {
        val TAG = this::class.simpleName

        fun newInstance(
            onCameraClick: () ->Unit,
            onVideoClick: () -> Unit
        ) = CameraDialog().apply {
            this.onCameraClick = onCameraClick
            this.onVideoClick = onVideoClick
        }

    }




}
