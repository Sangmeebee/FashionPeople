package com.sangmee.fashionpeople.observer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.repository.FUserRepository
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import com.sangmee.fashionpeople.util.SingleLiveEvent

class OtherViewModel : ViewModel() {
    private val fUserRepository: FUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }
    val profileImgName = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val followerNum = MutableLiveData<Int>(0)
    val followingNum = MutableLiveData<Int>(0)
    val isFollowing = MutableLiveData<Boolean>()
    val followBtnEvent = SingleLiveEvent<Int>()
    val callActivity = SingleLiveEvent<Int>()

    fun callProfile(customId: String) {
        //프로필 세팅
        fUserRepository.getFUser(customId, success = {
            profileImgName.value = it.profileImage
            userName.value = it.name
            followerNum.value = it.followerNum
            followingNum.value = it.followingNum
        }, failed = { Log.e("CALL_PROFILE_ERROR", it) })
    }

    fun clickFollowBtn(fragmentId : Int){
        followBtnEvent.value = fragmentId
    }

    fun callOtherActivity(num: Int) {
        callActivity.value = num
    }
}
