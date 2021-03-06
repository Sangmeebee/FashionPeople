package com.sangmee.fashionpeople.observer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FollowRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.repository.FollowRepository
import com.sangmee.fashionpeople.data.repository.FollowRepositoryImpl
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class FollowViewModel : ViewModel() {
    private val followRepository: FollowRepository by lazy {
        FollowRepositoryImpl(
            FollowRemoteDataSourceImpl()
        )
    }
    private val compositeDisposable = CompositeDisposable()

    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    val customId = GlobalApplication.prefs.getString("${loginType}_custom_id", "empty")
    val followers = MutableLiveData<List<FUser>>()
    val followings = MutableLiveData<List<FUser>>()

    //팔로워의 팔로잉 여부
    val isFollowingsFollower = MutableLiveData<MutableMap<String, Boolean>>()
    //팔로잉의 팔로잉 여부
    val isFollowingsFollowing = MutableLiveData<MutableMap<String, Boolean>>()
    val callActivity = SingleLiveEvent<String>()
    val isFollowerComplete = SingleLiveEvent<Any>()
    val isFollowingComplete = SingleLiveEvent<Any>()

    fun callFollower(userId: String) {
        followRepository.getFollower(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { isFollowerComplete.call() }
            .subscribe({ followers.value = it }, { t -> Log.e("error", t.message.toString()) })
            .addTo(compositeDisposable)

    }

    fun callFollowing(userId: String) {
        followRepository.getFollowing(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { isFollowingComplete.call() }
            .subscribe({ followings.value = it }, { t -> Log.e("error", t.message.toString()) })
            .addTo(compositeDisposable)
    }

    fun callFollowingsFollowing(customId: String) {
        followRepository.getIsFollowingsFollowing(
            this.customId,
            customId,
            {
                isFollowingsFollowing.value = it as MutableMap<String, Boolean>
            },
            { Log.e("error", it) })
    }

    fun callFollowingsFollower(customId: String) {
        followRepository.getIsFollowingsFollower(
            this.customId,
            customId,
            { isFollowingsFollower.value = it as MutableMap<String, Boolean> },
            { Log.e("error", it) })
    }

    fun callOtherActivity(customId: String) {
        callActivity.value = customId
    }

    fun unBindViewModel() {
        compositeDisposable.clear()
    }
}
