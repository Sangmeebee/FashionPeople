package com.sangmee.fashionpeople.observer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.dataSource.remote.SaveImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.repository.SaveImageRepository
import com.sangmee.fashionpeople.data.repository.SaveImageRepositoryImpl
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers


class SavedImageViewModel : ViewModel() {

    private val saveImageRepository: SaveImageRepository by lazy {
        SaveImageRepositoryImpl(SaveImageRemoteDataSourceImpl())
    }

    private val compositeDisposable = CompositeDisposable()

    val savedImages = MutableLiveData<List<FeedImage>>()
    val isComplete = SingleLiveEvent<Any>()

    fun callSavedImages(userId: String) {
        saveImageRepository.getSaveImages(userId)
        .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate { isComplete.call() }
            .subscribe({
                savedImages.value = it
            }, {
            }).addTo(compositeDisposable)
    }

    fun unBindDisposable() {
        compositeDisposable.clear()
    }
}
