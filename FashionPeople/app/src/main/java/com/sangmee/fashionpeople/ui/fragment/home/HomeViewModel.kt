package com.sangmee.fashionpeople.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    val homePage = MutableLiveData(0)
    val evaluatedIsAdded = MutableLiveData(false)
    val followingIsAdded = MutableLiveData(false)
}
