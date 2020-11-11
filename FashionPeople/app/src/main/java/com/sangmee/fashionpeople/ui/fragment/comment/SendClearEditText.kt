package com.sangmee.fashionpeople.ui.fragment.comment

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.sangmee.fashionpeople.R

class SendClearEditText : androidx.appcompat.widget.AppCompatEditText {

    constructor(context: Context) : super(context, null) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttrs: Int) : super(
        context,
        attrs,
        defStyleAttrs
    ) {
        init()
    }

    private fun init() {
        val tempDrawable = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24)
        val clearDrawable = tempDrawable?.let { DrawableCompat.wrap(it) }
        clearDrawable?.let {
            DrawableCompat.setTintList(it, hintTextColors)
        }
        clearDrawable?.intrinsicWidth?.let { width ->
            clearDrawable.setBounds(
                0, 0,
                width, clearDrawable.intrinsicHeight
            )
        }



    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {

        } else {

        }
    }


}