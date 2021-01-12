package com.sangmee.fashionpeople.ui.fragment.search

import android.graphics.Point
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sangmee.fashionpeople.R
import kotlinx.android.synthetic.main.fragment_search_brand.*

class SearchBrandFragment : Fragment() {

    private val searchBrandAdapter by lazy { SearchBrandAdapter() }
    private val vm by viewModels<SearchBrandViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_brand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()

    }

    private fun setRecyclerView() {
        val height = getDisplayHeight()
        rv_brand.apply {
            setHasFixedSize(true)
            minimumHeight = height
            adapter = searchBrandAdapter
        }
    }

    private fun crossfade() {
        rv_brand?.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(500L)
                .setListener(null)
        }
    }

    private fun getDisplayHeight(): Int {
        val display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        var height = size.y
        val resourceId = resources.getIdentifier(
            "design_bottom_navigation_height",
            "dimen",
            requireContext().packageName
        )
        if (resourceId > 0) {
            val navigationBarHeight = resources.getDimensionPixelSize(resourceId)
            height -= navigationBarHeight
        }
        return height
    }
}
