package com.sangmee.fashionpeople.ui.fragment.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.util.SingleLiveEvent

class SearchViewModel : ViewModel() {
    val etText = MutableLiveData<String>()

    val closeKeyBoard = SingleLiveEvent<Any>()
}
