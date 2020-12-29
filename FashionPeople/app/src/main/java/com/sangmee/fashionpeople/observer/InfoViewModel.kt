package com.sangmee.fashionpeople.observer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.FollowRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.repository.FUserRepository
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import com.sangmee.fashionpeople.data.repository.FollowRepository
import com.sangmee.fashionpeople.data.repository.FollowRepositoryImpl
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

class InfoViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val loadingSubject = BehaviorSubject.createDefault(false)

    private val fUserRepository: FUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }

    private val followRepository: FollowRepository by lazy {
        FollowRepositoryImpl(
            FollowRemoteDataSourceImpl()
        )
    }

    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    val customId = GlobalApplication.prefs.getString("${loginType}_custom_id", "empty")
    val profileImgName = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val introduce = MutableLiveData<String?>()
    val followerNum = MutableLiveData<Int>(0)
    val followingNum = MutableLiveData<Int>(0)
    val callActivity = SingleLiveEvent<Int>()
    val isFollowing = MutableLiveData<Boolean>()
    val followBtnEvent = SingleLiveEvent<Unit>()
    val galleryBtnEvent = SingleLiveEvent<Unit>()
    val isInvisible = MutableLiveData<Boolean>(false)
    val publishSubject = PublishSubject.create<Unit>()

    fun callProfile(userId: String) {
        //프로필 세팅
        fUserRepository.getFUser(userId, success = {
            profileImgName.value = it.profileImage
            introduce.value = it.introduce
            userName.value = it.name
            followerNum.value = it.followers?.size
            followingNum.value = it.followings?.size
        }, failed = { Log.e("CALL_PROFILE_ERROR", it) })

    }

    fun callIsFollowing(customId: String) {
        followRepository.getIsFollowing(
            this.customId,
            customId,
            { isFollowing.value = it },
            { Log.e("CALL_IS_FOLLOWING_ERROR", it) })
    }

    fun callOtherActivity(num: Int) {
        callActivity.value = num
    }

    fun clickFollowBtn() {
        followBtnEvent.value = Unit
    }

    fun updateFollowing(followingId: String) {
        followRepository.updateFollowing(customId, followingId, success = {
            Log.d("ADD_FOLLOWING", "팔로잉 추가")
        }, failed = { Log.d("ADD_FOLLOWING", "error") })
    }

    fun deleteFollowing(followingId: String) {
        followRepository.deleteFollowing(customId, followingId, success = {
            Log.d("DELETE_FOLLOWING", "팔로잉 삭제")
        }, failed = { Log.d("DELETE_FOLLOWING", "error") })
    }

    fun clickGalleryBtn() {
        galleryBtnEvent.value = Unit
    }


    fun deleteUser(customId: String) {
        fUserRepository.deleteUser(customId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingSubject.onNext(true) }
            .doAfterTerminate { loadingSubject.onNext(false) }
            .subscribe({

            }, {

            }).addTo(compositeDisposable)
    }

    fun unbindViewModel() {
        compositeDisposable.clear()
    }
}

