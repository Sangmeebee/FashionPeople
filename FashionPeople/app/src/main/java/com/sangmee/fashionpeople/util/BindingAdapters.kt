package com.sangmee.fashionpeople.util

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.retrofit.model.FeedImage
import org.jetbrains.anko.custom.style

@RequiresApi(Build.VERSION_CODES.M)
@BindingAdapter("createView")
fun createView(linearLayout: LinearLayout, feedImage: FeedImage?) {
    feedImage?.style?.let {
        val tv = TextView(linearLayout.context)
        tv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        tv.text = it
        tv.setTextAppearance(R.style.HomeFeedTextStyle)
        linearLayout.addView(tv)
        Log.d("seunghwan", it)
    }
    feedImage?.top?.let {
        val tv = TextView(linearLayout.context)
        tv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        tv.text = it
        tv.setTextAppearance(R.style.HomeFeedTextStyle)
        linearLayout.addView(tv)
        Log.d("seunghwan", it)
    }
    feedImage?.pants?.let {
        val tv = TextView(linearLayout.context)
        tv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        tv.text = it
        tv.setTextAppearance(R.style.HomeFeedTextStyle)
        linearLayout.addView(tv)
        Log.d("seunghwan", it)
    }
    feedImage?.shoes?.let {
        val tv = TextView(linearLayout.context)
        tv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        tv.text = it
        tv.setTextAppearance(R.style.HomeFeedTextStyle)
        linearLayout.addView(tv)
        Log.d("seunghwan", it)
    }
    Log.d("seunghwan", feedImage.toString())
}
