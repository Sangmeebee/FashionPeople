package com.sangmee.fashionpeople.ui.fragment.home.evaluate

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
import io.reactivex.rxjava3.subjects.AsyncSubject

class HomeEvaluateViewModel : ViewModel() {

    private val feedImageRepository = FeedImageRepositoryImpl(FeedImageRemoteDataSourceImpl())
    private val saveImageRepository = SaveImageRepositoryImpl(SaveImageRemoteDataSourceImpl())
    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    val userId = GlobalApplication.prefs.getString("${loginType}_custom_id", "empty")

    private val compositeDisposable = CompositeDisposable()

    private val _evaluateFeedImages = MutableLiveData<List<FeedImage>>()
    val evaluateFeedImages: LiveData<List<FeedImage>>
        get() = _evaluateFeedImages

    private val _followingFeedImages = MutableLiveData<List<FeedImage>>()
    val followingFeedImages: LiveData<List<FeedImage>>
        get() = _followingFeedImages

    private val _updateFeedImage = MutableLiveData<FeedImage>()
    val updateFeedImage: LiveData<FeedImage>
        get() = _updateFeedImage

    val evaluateLoadingComplete = SingleLiveEvent<Any>()
    val followingLoadingComplete = SingleLiveEvent<Any>()

    val saveComplete = SingleLiveEvent<Any>()
    val errorComplete = SingleLiveEvent<Any>()
    val deleteComplete = SingleLiveEvent<Any>()

    val asyncSubject = AsyncSubject.create<Unit>()


    fun getOtherImages() {
        feedImageRepository.getOtherImages(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { evaluateLoadingComplete.call() }
            .subscribe({
                _evaluateFeedImages.value = it
            }, {
            }).addTo(compositeDisposable)
    }

    fun getFollowingImages() {
        feedImageRepository.getFollowingFeedImages(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { followingLoadingComplete.call() }
            .subscribe({
                _followingFeedImages.value = it
            }, { Log.d("Sangmeebee", "evaluate_${it.message}") }).addTo(compositeDisposable)
    }

    fun postSaveImage(imageName: String) {
        saveImageRepository.postSaveImage(userId, imageName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { saveComplete.call() }
            .subscribe({ }, { t -> Log.d("Sangmeebee", "postComplete_${t.message}") })
            .addTo(compositeDisposable)
    }

    fun deleteSaveImage(imageName: String) {
        saveImageRepository.deleteSaveImage(userId, imageName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { deleteComplete.value = true }
            .subscribe({ }, { t -> Log.d("Sangmeebee", "deleteComplete_${t.message}") })
            .addTo(compositeDisposable)
    }

    fun ratingClick(imageName: String, rating: Float) {
        feedImageRepository.updateImageScore(imageName, Evaluation(userId, rating))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getFeedImage(imageName)
            }, {
                getFeedImage(imageName)
                errorComplete.call()
            }).addTo(compositeDisposable)
    }

    private fun getFeedImage(imageName: String) {
        RetrofitClient.getFeedImageService().getFeedImageByName(imageName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _updateFeedImage.value = it
            }, {
            }).addTo(compositeDisposable)
    }


    fun clearDisposable() {
        compositeDisposable.clear()
    }
}
