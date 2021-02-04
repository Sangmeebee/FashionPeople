package com.sangmee.fashionpeople.observer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.S3RemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.repository.FeedImageRepository
import com.sangmee.fashionpeople.data.repository.FeedImageRepositoryImpl
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class FeedImageViewModel : ViewModel() {

    private val feedImageRepository: FeedImageRepository by lazy {
        FeedImageRepositoryImpl(FeedImageRemoteDataSourceImpl())
    }

    private val compositeDisposable = CompositeDisposable()

    val feedImages = MutableLiveData<List<FeedImage>>()
    val isComplete = SingleLiveEvent<Any>()
    val deleteComplete = SingleLiveEvent<Any>()

    val behaviorSubject = BehaviorSubject.create<String>()


    fun callFeedImages(userId: String) {
        feedImageRepository.getFeedImages(
            userId
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate { isComplete.call() }
            .subscribe({
                feedImages.value = it
            }, {
            }).addTo(compositeDisposable)
    }

    fun deleteFeedImage(imageName: String, userId: String) {
        feedImageRepository.deleteFeedImage(imageName)
            .doOnSubscribe { behaviorSubject.onNext(imageName) }
            .andThen(feedImageRepository.getFeedImages(userId))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                deleteComplete.call()
                feedImages.value = it
            }, { Log.e("Sangmeebee", it.message.toString()) }).addTo(compositeDisposable)
    }

    fun unBindDisposable() {
        compositeDisposable.clear()
    }
}
