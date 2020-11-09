package com.sangmee.fashionpeople.util

import android.os.Build
import android.view.View
import android.widget.LinearLayout
import android.widget.RatingBar
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.sangmee.fashionpeople.data.model.FeedImage

@RequiresApi(Build.VERSION_CODES.M)
@BindingAdapter("createView")
fun createView(linearLayout: LinearLayout, feedImage: FeedImage?) {
    feedImage?.style?.let {
        createView(linearLayout, it)
    }
    feedImage?.top?.let {
        createView(linearLayout, it)
    }
    feedImage?.pants?.let {
        createView(linearLayout, it)
    }
    feedImage?.shoes?.let {
        createView(linearLayout, it)
    }
}

@BindingAdapter("setVisibleRating", "myId")
fun setVisibleRating(ratingBar: RatingBar, feedImage: FeedImage?, myId: String) {
    feedImage?.let {
        myId.let {
            feedImage.evaluations?.let {
                for (evaluation in feedImage.evaluations) {
                    if (evaluation.evaluationPersonId == myId) {
                        ratingBar.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }
}
