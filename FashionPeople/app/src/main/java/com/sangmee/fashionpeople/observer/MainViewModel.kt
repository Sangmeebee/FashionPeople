package com.sangmee.fashionpeople.observer

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.FollowRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.SaveImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.Evaluation
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.model.stack.Stack
import com.sangmee.fashionpeople.data.repository.*
import com.sangmee.fashionpeople.data.service.retrofit.RetrofitClient
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val saveImageRepository: SaveImageRepository by lazy {
        SaveImageRepositoryImpl(SaveImageRemoteDataSourceImpl())
    }
    private val feedImageRepository = FeedImageRepositoryImpl(FeedImageRemoteDataSourceImpl())
    private val followRepository = FollowRepositoryImpl(FollowRemoteDataSourceImpl())
    private val userRepository = FUserRepositoryImpl(FUserRemoteDataSourceImpl())

    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    var userId = GlobalApplication.prefs.getString("${loginType}_custom_id", "empty")

    val user = MutableLiveData<FUser>()
    val saveImages = MutableLiveData<List<FeedImage>>()

    val tagName = MutableLiveData<String>()

    val saveComplete = SingleLiveEvent<Any>()
    val callSaveImageComplete = SingleLiveEvent<Any>()
    val deleteComplete = SingleLiveEvent<Any>()
    val updateFeedImage = MutableLiveData<FeedImage>()

    val homeFragments = Stack<Fragment>()
    val rankFragments = Stack<Fragment>()
    val searchFragments = Stack<Fragment>()
    val infoFragments = Stack<Fragment>()
    val tagList = Stack<String>()

    //팔로워의 팔로잉 여부
    val isFollowingsFollower = MutableLiveData<MutableMap<String, Boolean>>()

    //팔로잉의 팔로잉 여부
    val isFollowingsFollowing = MutableLiveData<MutableMap<String, Boolean>>()
    val followerNum = MutableLiveData(0)
    val followingNum = MutableLiveData(0)

    //댓글 개수
    val comments = MutableLiveData<MutableMap<String, Int>>(mutableMapOf())

    fun getUser() {
        userRepository.getFUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ user.value = it }, {})
    }

    fun getMySaveImage() {
        saveImageRepository.getSaveImages(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate { callSaveImageComplete.call() }
            .subscribe({
                saveImages.value = it
            }, {
            }).addTo(compositeDisposable)
    }


    fun postSaveImage(imageName: String) {
        saveImageRepository.postSaveImage(userId, imageName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { saveComplete.call() }
            .subscribe({ }, { t -> Log.d("Sangmeebee", "evaluate_${t.message}") })
            .addTo(compositeDisposable)
    }

    fun deleteSaveImage(imageName: String) {
        saveImageRepository.deleteSaveImage(userId, imageName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { deleteComplete.call() }
            .subscribe({ }, { Log.e("Sangmeebee", it.message.toString()) })
            .addTo(compositeDisposable)
    }

    fun ratingClick(imageName: String, rating: Float) {
        feedImageRepository.updateImageScore(imageName, Evaluation(userId, rating))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getFeedImage(imageName)
            }, {
                Log.e("Sangmeebee", it.message.toString())
            }).addTo(compositeDisposable)
    }

    private fun getFeedImage(imageName: String) {
        RetrofitClient.getFeedImageService().getFeedImageByName(imageName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                updateFeedImage.value = it
            }, {
            }).addTo(compositeDisposable)
    }

    fun updateFollowing(followingId: String) {
        followRepository.updateFollowing(userId, followingId, success = {
            Log.d("ADD_FOLLOWING", "팔로잉 추가")
        }, failed = { Log.d("ADD_FOLLOWING", "error") })
    }

    fun deleteFollowing(followingId: String) {
        followRepository.deleteFollowing(userId, followingId, success = {
            Log.d("DELETE_FOLLOWING", "팔로잉 삭제")
        }, failed = { Log.d("DELETE_FOLLOWING", "error") })
    }

    fun callFollowingsFollowing() {
        followRepository.getIsFollowingsFollowing(
            userId, userId,
            { isFollowingsFollowing.value = it as MutableMap<String, Boolean> },
            { Log.e("error", it) })
    }

    fun callFollowingsFollower() {
        followRepository.getIsFollowingsFollower(
            userId, userId,
            { isFollowingsFollower.value = it as MutableMap<String, Boolean> },
            { Log.e("error", it) })
    }

    fun unBindViewModel() {
        compositeDisposable.clear()
    }
}
