package com.sangmee.fashionpeople.util

import android.os.Build
import android.view.View
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.ui.fragment.home.TagRecyclerViewAdapter

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
    comments?.let {
        textView.text = textView.context.getString(R.string.comment_title, comments.size)
    }
}

@BindingAdapter("setFeedImageTags")
fun setFeedImageTags(recyclerView: RecyclerView, feedImage: FeedImage?) {
    val tagAdapter = TagRecyclerViewAdapter()
    val layoutManager = FlexboxLayoutManager(recyclerView.context).apply {
        flexWrap = FlexWrap.WRAP
        flexDirection = FlexDirection.ROW
        justifyContent = JustifyContent.FLEX_START
    }
    recyclerView.adapter = tagAdapter
    recyclerView.layoutManager = layoutManager
    feedImage?.let { feedImage ->
        feedImage.style?.let {
            tagAdapter.addTag(it)
        }
        feedImage.top?.let {
            tagAdapter.addTag(it)
        }
        feedImage.pants?.let {
            tagAdapter.addTag(it)
        }
        feedImage.shoes?.let {
            tagAdapter.addTag(it)
        }
        tagAdapter.notifyDataSetChanged()
    }
}