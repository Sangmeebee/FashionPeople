package com.sangmee.fashionpeople.util

import android.os.Build
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.sangmee.fashionpeople.R

@RequiresApi(Build.VERSION_CODES.M)
fun createView(flexBoxLayout: FlexboxLayout, text: String) {
    if(!text.equals("")) {
        val tv = TextView(flexBoxLayout.context)
        tv.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        tv.text = "#$text"
        tv.setTextAppearance(R.style.HomeFeedTextStyle)
        flexBoxLayout.addView(tv)
    }
}
