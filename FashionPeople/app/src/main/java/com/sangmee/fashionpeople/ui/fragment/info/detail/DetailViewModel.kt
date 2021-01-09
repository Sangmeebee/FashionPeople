package com.sangmee.fashionpeople.ui.fragment.info.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.SaveImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.repository.FeedImageRepository
import com.sangmee.fashionpeople.data.repository.FeedImageRepositoryImpl
import com.sangmee.fashionpeople.data.repository.SaveImageRepository
import com.sangmee.fashionpeople.data.repository.SaveImageRepositoryImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class DetailViewModel : ViewModel() {

    private val feedImageRepository: FeedImageRepository by lazy{
        FeedImageRepositoryImpl(FeedImageRemoteDataSourceImpl())
    }

    private val saveImageRepository: SaveImageRepository by lazy {
        SaveImageRepositoryImpl(SaveImageRemoteDataSourceImpl())
    }

    private val compositeDisposable = CompositeDisposable()

    private val _feedImages = MutableLiveData<List<FeedImage>>()
    val feedImages: LiveData<List<FeedImage>>
        get() = _feedImages

    val isComplete = MutableLiveData(false)

    fun getImages(id: String) {
        feedImageRepository.getFeedImages(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { isComplete.value = true }
            .subscribe({
                _feedImages.value = it
            }, {
            }).addTo(compositeDisposable)
    }

    fun getSaveImages(id: String) {
        saveImageRepository.getSaveImages(id)
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
