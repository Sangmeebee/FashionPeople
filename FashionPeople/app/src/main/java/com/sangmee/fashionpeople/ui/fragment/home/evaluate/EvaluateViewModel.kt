package com.sangmee.fashionpeople.ui.fragment.home.evaluate

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.service.retrofit.RetrofitClient
import com.sangmee.fashionpeople.data.model.Evaluation
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.repository.FUserRepository
import com.sangmee.fashionpeople.data.repository.FeedImageRepository
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class EvaluateViewModel(
    private val feedImageRepository: FeedImageRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _feedImages = MutableLiveData<List<FeedImage>>()
    val feedImages: LiveData<List<FeedImage>>
        get() = _feedImages

    private val _updateFeedImage = MutableLiveData<FeedImage>()
    val updateFeedImages: LiveData<FeedImage>
        get() = _updateFeedImage

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId

    private val _nowPage = MutableLiveData<Int>()
    val nowPage: LiveData<Int>
        get() = _nowPage

    private val _evaluateMessage = SingleLiveEvent<Unit>()
    val evaluateMessage: LiveData<Unit>
        get() = _evaluateMessage

    val idSubject = BehaviorSubject.create<String>()

    init {
        idSubject.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _userId.value = it
                getOtherImages(it)
            }, {
            }).addTo(compositeDisposable)
    }

    private fun getOtherImages(id: String) {
        feedImageRepository.getOtherImages(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _feedImages.value = it
            }, {
            }).addTo(compositeDisposable)
    }

    fun ratingClick(imageName: String, rating: Float) {
        feedImageRepository.updateImageScore(imageName, Evaluation(userId.value, rating))
            .subscribeOn(Schedulers.io())
            .andThen(RetrofitClient.getFeedImageService().getFeedImageByName(imageName))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it?.let {
                    _updateFeedImage.value = it
                }
                Log.d("seunghwan", it.toString())
                _evaluateMessage.call()
            }, {
                Log.d("seunghwan", it.toString())
            }).addTo(compositeDisposable)
    }

    fun setNextPage() {
        _nowPage.value = _nowPage.value?.plus(1)
    }

    fun clearDisposable() {
        compositeDisposable.clear()
    }
}
