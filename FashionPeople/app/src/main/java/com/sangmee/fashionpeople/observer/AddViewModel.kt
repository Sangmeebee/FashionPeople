package com.sangmee.fashionpeople.observer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.repository.FeedImageRepository
import com.sangmee.fashionpeople.data.repository.FeedImageRepositoryImpl

class AddViewModel : ViewModel() {
    private val feedImageRepository: FeedImageRepository by lazy {
        FeedImageRepositoryImpl(FeedImageRemoteDataSourceImpl())
    }
    val customId = GlobalApplication.prefs.getString("custom_id", "empty")

    val evaluatedFeedImage = MutableLiveData<FeedImage>()

    fun callEvaluatedFeedImage() {
        feedImageRepository.getEvaluatedFeedImage(
            customId,
            { evaluatedFeedImage.value = it },
            { Log.d("CALL_EVALUATED_ERROR", it) })
    }
}
