package com.sangmee.fashionpeople.util

import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.Evaluation
import com.sangmee.fashionpeople.data.model.FeedImage

@RequiresApi(Build.VERSION_CODES.M)
fun createView(flexBoxLayout: FlexboxLayout, text: String) {
    if (!text.equals("")) {
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

fun Float.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    ).toInt()
}

fun getRatingFromEvaluations(evaluations: List<Evaluation>?): Float {
    return if (evaluations.isNullOrEmpty()) {
        0f
    } else {
        var sum = 0f
        for (i in evaluations.indices) {
            evaluations[i].score?.let {
                sum += it
            }
        }
        sum / evaluations.size
    }

}