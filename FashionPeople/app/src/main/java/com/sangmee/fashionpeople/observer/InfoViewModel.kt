package com.sangmee.fashionpeople.observer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.repository.FUserRepository
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import com.sangmee.fashionpeople.util.SingleLiveEvent

class InfoViewModel : ViewModel() {

    private val fUserRepository: FUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }
    val customId = GlobalApplication.prefs.getString("custom_id", "empty")
    val profileImgName = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val followerNum = MutableLiveData<Int>(0)
    val followingNum = MutableLiveData<Int>(0)
    val callActivity = SingleLiveEvent<Int>()
    val errorMsg = MutableLiveData<String>()

    fun callProfile() {
        //프로필 세팅
        if (customId !== "empty") {
            fUserRepository.getFUser(customId, success = {
                profileImgName.value = it.profileImage
                userName.value = it.name
                followerNum.value = it.followerNum
                followingNum.value = it.followingNum
            }, failed = { errorMsg.value = it })
        }
    }

    fun callOtherActivity(num: Int) {
        callActivity.value = num
    }
}

