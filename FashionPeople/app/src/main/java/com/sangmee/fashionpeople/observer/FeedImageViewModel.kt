package com.sangmee.fashionpeople.observer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.repository.FeedImageRepository
import com.sangmee.fashionpeople.data.repository.FeedImageRepositoryImpl

class FeedImageViewModel : ViewModel() {

    private val feedImageRepository: FeedImageRepository by lazy {
        FeedImageRepositoryImpl(FeedImageRemoteDataSourceImpl())
    }
    val feedImages = MutableLiveData<List<FeedImage>>()

    fun callFeedImages(userId: String) {
        feedImageRepository.getFeedImages(
            userId,
            success = { feedImages.value = it },
            failed = { Log.d("fail", it) })
    }

}
