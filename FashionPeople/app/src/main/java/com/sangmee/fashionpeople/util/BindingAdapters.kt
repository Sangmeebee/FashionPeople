package com.sangmee.fashionpeople.util

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.ui.SettingActivity

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

@BindingAdapter("setVisibleLinearLayout", "myId")
fun setVisibleLinearLayout(linearLayout: LinearLayout, feedImage: FeedImage?, myId: String) {
    feedImage?.let {
        myId.let {
            feedImage.evaluations?.let {
                for (evaluation in feedImage.evaluations) {
                    if (evaluation.evaluationPersonId == myId) {
                        linearLayout.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}

@BindingAdapter("setCustomId", "setImageName")
fun ImageView.setLoadUrl(customId: String, imageName: String) {
    Glide.with(context!!)
        .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${customId}/profile/${imageName}")
        .apply(RequestOptions().circleCrop())
        .error(R.drawable.user).into(this)
}


