package com.sangmee.fashionpeople.util

import android.os.Build
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.sangmee.fashionpeople.R

@RequiresApi(Build.VERSION_CODES.M)
fun createView(linearLayout: LinearLayout, text: String) {
    if(!text.equals("")) {
        val tv = TextView(linearLayout.context)
        tv.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        tv.text = "#$text"
        tv.setTextAppearance(R.style.HomeFeedTextStyle)
        linearLayout.addView(tv)
    }
}
