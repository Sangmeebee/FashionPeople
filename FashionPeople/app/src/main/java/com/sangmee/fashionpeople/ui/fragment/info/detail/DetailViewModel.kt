package com.sangmee.fashionpeople.ui.fragment.info.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.repository.FeedImageRepository
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

    val idSubject = BehaviorSubject.create<String>()
    val isComplete = MutableLiveData(false)

    init {
        idSubject.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getImages(it)
            }, {
            }).addTo(compositeDisposable)
    }

    private fun getImages(id: String) {
        feedImageRepository.getFeedImages(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { isComplete.value = true }
            .subscribe({
                _feedImages.value = it
            }, {
            }).addTo(compositeDisposable)
    }

    fun clearDisposable() {
        compositeDisposable.clear()
    }
}
