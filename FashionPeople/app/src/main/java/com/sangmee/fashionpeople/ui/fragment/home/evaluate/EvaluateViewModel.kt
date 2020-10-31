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
import io.reactivex.subjects.BehaviorSubject

class EvaluateViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val feedImages = MutableLiveData<List<FeedImage>>()
    val idSubject = BehaviorSubject.create<String>()

    init {
        idSubject.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getOtherImages(it)
            }, {
            }).addTo(compositeDisposable)
    }

    private fun getOtherImages(id: String) {
        RetrofitClient().getFeedImageService().getOtherImages(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                feedImages.value = it
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