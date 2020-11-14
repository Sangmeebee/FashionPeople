package com.sangmee.fashionpeople.ui.fragment.home

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.sangmee.fashionpeople.util.dpToPx
import kotlin.properties.Delegates

class TagRecyclerDecoration(
    private val context: Context,
    private val _space: Float
): RecyclerView.ItemDecoration() {

    private var spacePx by Delegates.notNull<Int>()

    init {
        spacePx = _space.dpToPx(context)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val lp = view.layoutParams as FlexboxLayoutManager.LayoutParams
        lp.viewLayoutPosition


        outRect.left = spacePx
        outRect.right = spacePx
        outRect.bottom = spacePx
    }

}