package com.sangmee.fashionpeople.ui.add

import android.util.Log
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.dataSource.remote.BrandRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.repository.BrandRepositoryImpl
import com.sangmee.fashionpeople.data.repository.FeedImageRepositoryImpl
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class TagViewModel : ViewModel() {

    private val brandRepository = BrandRepositoryImpl(BrandRemoteDataSourceImpl())
    private val feedImageRepository = FeedImageRepositoryImpl(FeedImageRemoteDataSourceImpl())

    private val compositeDisposable = CompositeDisposable()

    val isComplete = SingleLiveEvent<Any>()
    val isError = SingleLiveEvent<Any>()


    fun postFeedImage(customId: String, feedImage: FeedImage) {
        feedImageRepository.postFeedImage(customId, feedImage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { isComplete.call() }
            .subscribe({ }, {
                Log.e("Sangmeebee", it.message.toString())
                isError.call()
            })
            .addTo(compositeDisposable)
    }

    fun putStyle(style: String) {
        brandRepository.putStyle(style)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ }, {
                Log.e("Sangmeebee", it.message.toString())
                isError.call()
            })
            .addTo(compositeDisposable)
    }

    fun putBrand(brand: String) {
        brandRepository.putBrand(brand)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ }, {
                Log.e("Sangmeebee", it.message.toString())
                isError.call()
            })
            .addTo(compositeDisposable)
    }

    fun unbindViewModel() {
        compositeDisposable.clear()
    }
}
