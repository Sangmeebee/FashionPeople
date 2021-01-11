package com.sangmee.fashionpeople.ui.fragment.search

import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.util.SingleLiveEvent

class SearchBrandViewModel : ViewModel() {

    val callFragment = SingleLiveEvent<Any>()


    fun callBrandFragment() {
        callFragment.call()
    }
}
