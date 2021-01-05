package com.sangmee.fashionpeople.observer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.repository.FeedImageRepository
import com.sangmee.fashionpeople.data.repository.FeedImageRepositoryImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class FeedImageViewModel : ViewModel() {

    private val feedImageRepository: FeedImageRepository by lazy {
        FeedImageRepositoryImpl(FeedImageRemoteDataSourceImpl())
    }

    private val compositeDisposable = CompositeDisposable()

    val feedImages = MutableLiveData<List<FeedImage>>()

    fun callFeedImages(userId: String) {
        feedImageRepository.getFeedImages(
            userId
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                feedImages.value = it
            }, {
            }).addTo(compositeDisposable)
    }

    fun unBindDisposable() {
        compositeDisposable.clear()
    }
}
