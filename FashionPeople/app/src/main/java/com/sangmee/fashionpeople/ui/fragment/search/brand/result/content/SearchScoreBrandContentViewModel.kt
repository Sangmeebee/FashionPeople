package com.sangmee.fashionpeople.ui.fragment.search.brand.result.content

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.repository.FeedImageRepositoryImpl
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchScoreBrandContentViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val feedImageRepository = FeedImageRepositoryImpl(FeedImageRemoteDataSourceImpl())

    val scoreBrandImages = MutableLiveData<List<FeedImage>>()
    val isComplete = SingleLiveEvent<Any>()
    val isAdded = MutableLiveData(false)

    fun callScoreBrandImages(query: String) {
        feedImageRepository.getSearchScoreBrandImages(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { isComplete.call() }
            .subscribe(
                { scoreBrandImages.value = it },
                { Log.d("Sangmeebee", it.message.toString()) })
            .addTo(compositeDisposable)
    }


    fun unbindViewModel() {
        compositeDisposable.clear()
    }
}
