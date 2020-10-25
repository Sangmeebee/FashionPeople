package com.sangmee.fashionpeople.ui.fragment.home.evaluate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.retrofit.RetrofitClient
import com.sangmee.fashionpeople.retrofit.model.FeedImage
import com.sangmee.fashionpeople.retrofit.service.FeedImageService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class EvaluateViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val feedImages = MutableLiveData<List<FeedImage>>()

    private fun getAllImages() {
        RetrofitClient.getFeedImageService().getAllFeedImages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                feedImages.value = it
            }, {

            }).addTo(compositeDisposable)
    }



    fun clearDisposable() {
        compositeDisposable.clear()
    }
}