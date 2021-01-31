package com.sangmee.fashionpeople.observer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.FollowRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FUser
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
    val gender = MutableLiveData<String>()
    val height = MutableLiveData<Int>()
    val weight = MutableLiveData<Int>()
    val introduce = MutableLiveData<String?>()
    val followerNum = MutableLiveData(0)
    val followingNum = MutableLiveData(0)
    val callActivity = SingleLiveEvent<Int>()
    val followBtnEvent = SingleLiveEvent<Unit>()
    val galleryBtnEvent = SingleLiveEvent<Unit>()
    val profileReviseBtnEvent = SingleLiveEvent<Unit>()
    val behaviorSubject = BehaviorSubject.create<Unit>()
    var isCallProfileComplete = SingleLiveEvent<Any>()
    val isFollowing = MutableLiveData<Boolean>()

    fun callProfile(userId: String) {
        //프로필 세팅
        fUserRepository.getFUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate { isCallProfileComplete.call() }
            .subscribe({
                profileImgName.value = it.profileImage
                introduce.value = it.introduce
                userName.value = it.name
                gender.value = it.gender
                height.value = it.height
                weight.value = it.weight
                followerNum.value = it.followers?.size
                followingNum.value = it.followings?.size
            }, { t ->
                Log.e("CALL_PROFILE_ERROR", t.message.toString())
            }).addTo(compositeDisposable)

    }

    fun updateProfile(customId: String, fUser: FUser) {
        fUserRepository.updateUser(customId, fUser)
            .subscribeOn(Schedulers.io())
            .subscribe { }.addTo(compositeDisposable)
    }



    fun callOtherActivity(num: Int) {
        callActivity.value = num
    }

    fun clickFollowBtn() {
        followBtnEvent.value = Unit
    }

    fun callIsFollowing(customId: String) {
        followRepository.getIsFollowing(
            this.customId,
            customId,
            { isFollowing.value = it },
            { Log.e("CALL_IS_FOLLOWING_ERROR", it) })
    }

    fun clickGalleryBtn() {
        galleryBtnEvent.value = Unit
    }

    fun clickProfileReviseBtn() {
        profileReviseBtnEvent.value = Unit
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

