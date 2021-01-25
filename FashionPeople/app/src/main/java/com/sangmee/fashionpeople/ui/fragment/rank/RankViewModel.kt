package com.sangmee.fashionpeople.ui.fragment.rank

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RankViewModel : ViewModel() {

    val rankPage = MutableLiveData(0)
    val manIsAdded = MutableLiveData(false)
    val womanIsAdded = MutableLiveData(false)
}
