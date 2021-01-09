package com.sangmee.fashionpeople.observer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.SaveImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.Evaluation
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.repository.FeedImageRepositoryImpl
import com.sangmee.fashionpeople.data.repository.SaveImageRepositoryImpl
import com.sangmee.fashionpeople.data.service.retrofit.RetrofitClient
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    private val feedImageRepository = FeedImageRepositoryImpl(FeedImageRemoteDataSourceImpl())
    private val saveImageRepository = SaveImageRepositoryImpl(SaveImageRemoteDataSourceImpl())

    private val compositeDisposable = CompositeDisposable()

    private val _evaluateFeedImages = MutableLiveData<List<FeedImage>>()
    val evaluateFeedImages: LiveData<List<FeedImage>>
        get() = _evaluateFeedImages

    val evaluateFeedImagesIsSaved = MutableLiveData<MutableMap<String, Boolean>>()

    private val _followingFeedImages = MutableLiveData<List<FeedImage>>()
    val followingFeedImages: LiveData<List<FeedImage>>
        get() = _followingFeedImages

    val followingFeedImagesIsSaved = MutableLiveData<MutableMap<String, Boolean>>()

    private val _savedFeedImages = MutableLiveData<List<FeedImage>>()
    val savedFeedImages: LiveData<List<FeedImage>>
        get() = _savedFeedImages

    private val _updateFeedImage = MutableLiveData<FeedImage>()
    val updateFeedImage: LiveData<FeedImage>
        get() = _updateFeedImage

    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    val userId = GlobalApplication.prefs.getString("${loginType}_custom_id", "empty")

    val evaluateLoadingComplete = MutableLiveData(false)
    val followingLoadingComplete = MutableLiveData(false)

    val saveComplete = SingleLiveEvent<Boolean>()

    val deleteComplete = SingleLiveEvent<Boolean>()

    fun getOtherImages() {
        feedImageRepository.getOtherImages(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { getSaveImages() }
            .subscribe({
                _evaluateFeedImages.value = it
            }, {
            }).addTo(compositeDisposable)
    }

    private fun getSaveImages() {
        saveImageRepository.getSaveImages(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ _savedFeedImages.value = it }, {}).addTo(compositeDisposable)
    }

    fun getFollowingImages() {
        feedImageRepository.getFollowingFeedImages(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { getSaveImages() }
            .subscribe({
                _followingFeedImages.value = it
            }, { Log.d("Sangmeebee", "evaluate_${it.message}") }).addTo(compositeDisposable)
    }

    fun postSaveImage(imageName: String) {
        saveImageRepository.postSaveImage(userId, imageName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { saveComplete.value = true }
            .subscribe({ }, { t -> Log.d("Sangmeebee", "evaluate_${t.message}") })
            .addTo(compositeDisposable)
    }

    fun deleteSaveImage(imageName: String) {
        saveImageRepository.deleteSaveImage(userId, imageName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { deleteComplete.value = true }
            .subscribe { }.addTo(compositeDisposable)
    }

    fun ratingClick(imageName: String, rating: Float) {
        feedImageRepository.updateImageScore(imageName, Evaluation(userId, rating))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getFeedImage(imageName)
            }, {
            }).addTo(compositeDisposable)
    }

    private fun getFeedImage(imageName: String) {
        RetrofitClient.getFeedImageService().getFeedImageByName(imageName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it?.let {
                    _updateFeedImage.value = it
                }
            }, {
            }).addTo(compositeDisposable)
    }


    fun clearDisposable() {
        compositeDisposable.clear()
        Log.d("Sangmeebee", "clearDisposable")
    }
}
