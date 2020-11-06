package com.sangmee.fashionpeople.ui.fragment.home.evaluate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.retrofit.RetrofitClient
import com.sangmee.fashionpeople.retrofit.model.Evaluation
import com.sangmee.fashionpeople.retrofit.model.FeedImage
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class EvaluateViewModel : ViewModel() {

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
    val nowPageSubject = BehaviorSubject.create<Int>()

    init {
        idSubject.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _userId.value = it
                getOtherImages(it)
            }, {
            }).addTo(compositeDisposable)
        nowPageSubject.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _nowPage.value = it
            }, {
            }).addTo(compositeDisposable)
    }

    private fun getOtherImages(id: String) {
        RetrofitClient().getFeedImageService().getOtherImages(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _feedImages.value = it
            }, {
            }).addTo(compositeDisposable)
    }

    fun ratingClick(imageName: String, rating: Float) {
        RetrofitClient().getFeedImageService()
            .updateImageScore(imageName, Evaluation(userId.value, rating))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .andThen(RetrofitClient().getFeedImageService().getFeedImageByName(imageName))
            .subscribe({
                _nowPage.value = _nowPage.value?.plus(1)
                it?.let {
                    _updateFeedImage.value = it
                }
                _evaluateMessage.call();
            }, {
            }).addTo(compositeDisposable)
    }


    fun clearDisposable() {
        compositeDisposable.clear()
    }
}