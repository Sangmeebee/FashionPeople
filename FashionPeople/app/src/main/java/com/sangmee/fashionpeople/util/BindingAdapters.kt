package com.sangmee.fashionpeople.util

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.model.RankImage
import com.sangmee.fashionpeople.ui.fragment.home.TagRecyclerViewAdapter
import com.skydoves.progressview.ProgressView

@BindingAdapter("setVisibleComments")
fun setVisibleComments(recyclerView: RecyclerView, comments: List<Comment>?) {
    comments?.let {
        if (it.isNotEmpty()) {
            recyclerView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.GONE
        }
    }
}

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

@BindingAdapter("setFeedCustomId", "setFeedImageName")
fun ImageView.setFeedUrl(customId: String?, imageName: String?) {
    customId?.let { id ->
        imageName?.let { imageName ->
            Glide.with(context!!)
                .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${id}/feed/${imageName}")
                .error(R.drawable.ic_user).into(this)
        }
    }
}

@BindingAdapter("setVisibleEmptyView")
fun setVisibleEmptyView(textView: TextView, comments: List<Comment>?) {
    comments?.let {
        if (it.isNotEmpty()) {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
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

@BindingAdapter("setFeedImageTags")
fun setFeedImageTags(recyclerView: RecyclerView, feedImage: FeedImage?) {
    val tagAdapter = TagRecyclerViewAdapter()
    feedImage?.let { feedImage ->
        if (feedImage.style != null && !feedImage.style.equals("")) {
            tagAdapter.addTag(feedImage.style)
        }
        if (feedImage.top != null && !feedImage.top.equals("")) {
            tagAdapter.addTag(feedImage.top)
        }
        if (feedImage.pants != null && !feedImage.pants.equals("")) {
            tagAdapter.addTag(feedImage.pants)
        }
        if (feedImage.shoes != null && !feedImage.shoes.equals("")) {
            tagAdapter.addTag(feedImage.shoes)
        }
        tagAdapter.notifyDataSetChanged()
    }
    val layoutManager = FlexboxLayoutManager(recyclerView.context).apply {
        flexWrap = FlexWrap.WRAP
        flexDirection = FlexDirection.ROW
        justifyContent = JustifyContent.FLEX_START
    }

    recyclerView.adapter = tagAdapter
    recyclerView.layoutManager = layoutManager
}

@BindingAdapter("createTag")
fun createTag(appCompatTextView: AppCompatTextView, text: String?) {
    text?.let {
        appCompatTextView.text = "#${text}"
    }
}

@BindingAdapter("setSpannableRating")
fun setSpannableRating(appCompatTextView: AppCompatTextView, feedImage: FeedImage?) {

    var average = 0f
    feedImage?.let { feedImage ->
        feedImage.evaluations?.let {
            average = getRatingFromEvaluations(it)
        }
    }
    val target = String.format("%.1f점", average)

    val text = "${feedImage?.user?.name}님의 패션 점수는 $target 입니다."
    val spannableString = SpannableString(text)
    val targetStartIndex = text.indexOf(target)
    val targetEndIndex = targetStartIndex + target.length

    spannableString.setSpan(
        ForegroundColorSpan(appCompatTextView.resources.getColor(R.color.colorPrimary)),
        targetStartIndex,
        targetEndIndex,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    appCompatTextView.text = spannableString


}

@BindingAdapter("setRatingText")
fun setRatingText(appCompatTextView: AppCompatTextView, feedImage: FeedImage?) {
    var average = 0f
    feedImage?.let { feedImage ->
        feedImage.evaluations?.let {
            average = getRatingFromEvaluations(it)
        }
    }
    val text = String.format("%.1f", average)

    appCompatTextView.text = text
}

@BindingAdapter("setGradeRating")
fun setGradeRating(appCompatRatingBar: AppCompatRatingBar, feedImage: FeedImage?) {
    var average = 0f
    feedImage?.let { feedImage ->
        feedImage.evaluations?.let {
            average = getRatingFromEvaluations(it)
        }
    }
    appCompatRatingBar.rating = average
}

@BindingAdapter("setEvaluateProgress")
fun setEvaluateProgress(progressView: ProgressView, feedImage: FeedImage?) {
    var total = 0
    feedImage?.let { image ->
        image.evaluations?.let { list ->
            total = list.size
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

@BindingAdapter("isInvisible")
fun View.bindIsInvisible(isInvisible: Boolean) {
    this.visibility = if (isInvisible) {
        View.INVISIBLE
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
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
        this.text = "평가중"
    } else {
        this.text = resultScore
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27F)

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
