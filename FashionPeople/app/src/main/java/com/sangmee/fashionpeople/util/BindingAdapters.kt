package com.sangmee.fashionpeople.util

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Html
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.data.model.FeedImage
import com.skydoves.progressview.ProgressView

@BindingAdapter("setCustomId", "setImageName")
fun ImageView.setLoadUrl(customId: String?, imageName: String?) {
    customId?.let { id ->
        if (imageName.isNullOrEmpty()) {
            setImageResource(R.drawable.ic_user)
        } else {
            Glide.with(context!!)
                .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${id}/profile/${imageName}")
                .apply(RequestOptions().circleCrop())
                .error(R.drawable.ic_user).into(this)
        }
    }
}

@BindingAdapter("setCommentTitle")
fun setCommentTitle(textView: TextView, comments: List<Comment>?) {
    if (comments.isNullOrEmpty()) {
        textView.text = "댓글 0개"
    } else {
        textView.text =
            textView.context.getString(R.string.comment_title, comments.size)
    }
}

@BindingAdapter("setRatingText")
fun setRatingText(appCompatTextView: AppCompatTextView, feedImage: FeedImage?) {
    feedImage?.let {
        if (it.evaluateNow) {
            appCompatTextView.text = "평가중"
            appCompatTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18F)

        } else {
            var average = 0f
            it.evaluations?.let { evaluations ->
                average = getRatingFromEvaluations(evaluations)
            }
            val text = String.format("%.1f", average)

            appCompatTextView.text = text
        }
    }
}

@BindingAdapter("isGone")
fun View.bindIsGone(isGone: Boolean) {
    this.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}


@SuppressLint("SetTextI18n")
@RequiresApi(Build.VERSION_CODES.M)
@BindingAdapter("setTvBackGround")
fun setTvBackGround(textView: TextView, rank: Int) {

    val drawable =
        ContextCompat.getDrawable(textView.context, R.drawable.bg_ranking_text) as GradientDrawable

    if (rank == 0) {
        drawable.setColor(textView.context.getColor(R.color.colorPrimaryDark))
    } else if (rank == 1 || rank == 2) {
        drawable.setColor(textView.context.getColor(R.color.colorPrimary))
    } else {
        drawable.setColor(textView.context.getColor(R.color.colorBlack))
    }
    textView.background = drawable
    textView.text = "${rank + 1}위"
}


@BindingAdapter("setHTMLText")
fun TextView.setHtmlText(stringId: Int) {
    this.text = Html.fromHtml(String.format(this.context.getString(stringId)))
}

@BindingAdapter("setHeightText")
fun TextView.setHeightText(height: Int) {
    if (height != 0) {
        this.isVisible = true
        this.text = "${height}cm"
    } else {
        this.isVisible = false
    }
}

@BindingAdapter("setWeightText")
fun TextView.setWeightText(weight: Int) {
    if (weight != 0) {
        this.isVisible = true
        this.text = "${weight}kg"
    } else {
        this.isVisible = false
    }
}

@BindingAdapter("setResultScoreText", "isEvaluateNow")
fun TextView.setResultScoreText(resultScore: String, isEvaluateNow: Boolean) {
    if (isEvaluateNow) {
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22F)
        this.text = "평가중"
    } else {
        this.text = resultScore
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 29F)

    }
}

@BindingAdapter("setTag")
fun TextView.setTag(tag: String) {
    if (tag == "") {
        this.text = "선택안함"
    } else {
        this.text = tag
    }
}

@BindingAdapter("setColorTag")
fun TextView.setColorTag(tag: String) {
    if (tag == "") {
        this.text = "선택안함"
        this.setTextColor(ContextCompat.getColor(context, R.color.brandTextColor))
    } else {
        this.text = tag
        this.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
    }
}

@BindingAdapter("setIsEvaluated")
fun TextView.setIsEvaluated(isEvaluated: Boolean) {
    if (isEvaluated) {
        this.text = "평가중"
    } else {
        this.text = "평가완료"
    }
}

@BindingAdapter("setScore")
fun TextView.setScore(feedImage: FeedImage?) {
    var average = 0f
    feedImage?.let { image ->
        image.evaluations?.let {
            average = getRatingFromEvaluations(it)
        }
    }
    val text = String.format("%.1f", average)
    this.text = text
}
