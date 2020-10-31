package com.sangmee.fashionpeople.ui.fragment.home.evaluate

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.retrofit.RetrofitClient
import com.sangmee.fashionpeople.retrofit.model.FeedImage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class EvaluateViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val feedImages = MutableLiveData<List<FeedImage>>()

    init {
        getAllImages()
    }

    private fun getAllImages() {
        RetrofitClient().getFeedImageService().getAllFeedImages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                feedImages.value = it
                Log.d("seunghwan", it[0].style)
                Log.d("seunghwan", it[0].user?.name)
            }, {

            }).addTo(compositeDisposable)
    }

    private fun ratingClickEvent() {
        RetrofitClient().getFeedImageService()
    }



    fun clearDisposable() {
        compositeDisposable.clear()
    }
}