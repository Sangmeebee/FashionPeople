package com.sangmee.fashionpeople.ui.fragment.info.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.repository.FeedImageRepository
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class DetailViewModel(
    private val feedImageRepository: FeedImageRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _feedImages = MutableLiveData<List<FeedImage>>()
    val feedImages: LiveData<List<FeedImage>>
        get() = _feedImages

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId

    val idSubject = BehaviorSubject.create<String>()

    init {
        idSubject.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _userId.value = it
                getImages(it)
            }, {
            }).addTo(compositeDisposable)
    }

    private fun getImages(id: String) {
        feedImageRepository.getFeedImages(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _feedImages.value = it
            }, {
            }).addTo(compositeDisposable)
    }

    fun clearDisposable() {
        compositeDisposable.clear()
    }
}
