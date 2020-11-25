package com.sangmee.fashionpeople.util

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.*
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.model.RankImage
import com.sangmee.fashionpeople.ui.fragment.home.TagRecyclerViewAdapter
import com.sangmee.fashionpeople.ui.fragment.info.FeedImageDetailActivity
import com.skydoves.progressview.ProgressView


@RequiresApi(Build.VERSION_CODES.M)
@BindingAdapter("createView")
fun createView(flexBoxLayout: FlexboxLayout, feedImage: FeedImage?) {
    feedImage?.style?.let {
        createView(flexBoxLayout, it)
    }
    feedImage?.top?.let {
        createView(flexBoxLayout, it)
    }
    feedImage?.pants?.let {
        createView(flexBoxLayout, it)
    }
    feedImage?.shoes?.let {
        createView(flexBoxLayout, it)
    }
}

@BindingAdapter("setVisibleRating", "myId")
fun setVisibleRating(ratingBar: RatingBar, feedImage: FeedImage?, myId: String) {
    feedImage?.let {
        myId.let {
            feedImage.evaluations?.let {
                if(it.size >= 3) {
                    ratingBar.visibility = View.INVISIBLE
                }
                for (evaluation in feedImage.evaluations) {
                    if (evaluation.evaluationPersonId == myId) {
                        ratingBar.visibility = View.INVISIBLE
                        break;
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
                if(it.size >= 3) {
                    linearLayout.visibility = View.VISIBLE
                }
            }
        }
    }
}

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
        imageName?.let { imageName ->
            Glide.with(context!!)
                .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${id}/profile/${imageName}")
                .apply(RequestOptions().circleCrop())
                .error(R.drawable.user).into(this)
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
    val layoutManager = FlexboxLayoutManager(recyclerView.context).apply {
        flexWrap = FlexWrap.WRAP
        alignItems = AlignItems.FLEX_START
        flexDirection = FlexDirection.ROW
        justifyContent = JustifyContent.FLEX_START
    }

    val decoration = FlexboxItemDecoration(recyclerView.context)
    decoration.setOrientation(FlexboxItemDecoration.BOTH)
    val drawable = GradientDrawable().apply {
        setSize(
            5.toFloat().dpToPx(recyclerView.context),
            5.toFloat().dpToPx(recyclerView.context)
        )
    }
    decoration.setDrawable(drawable)

    recyclerView.adapter = tagAdapter
    recyclerView.layoutManager = layoutManager


    recyclerView.addItemDecoration(decoration)
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

@BindingAdapter("marginTop")
fun View.setMarginTop(marginTop: Float) {
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.topMargin = marginTop.toInt()
    this.layoutParams = layoutParams
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

@BindingAdapter("linkToDetail")
fun linkToDetail(constraintLayout: ConstraintLayout, rankImage: RankImage?) {
    rankImage?.feedImage?.let { feedImage ->
        constraintLayout.setOnClickListener {
            val intent = Intent(it.context, FeedImageDetailActivity::class.java)
            intent.putExtra(FeedImageDetailActivity.KEY_FEED_IMAGE, feedImage)
            it.context.startActivity(intent)
        }
    }
}