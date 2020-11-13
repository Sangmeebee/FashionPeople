package com.sangmee.fashionpeople.observer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FollowRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.repository.FollowRepository
import com.sangmee.fashionpeople.data.repository.FollowRepositoryImpl

class FollowerViewModel : ViewModel() {
    private val followRepository: FollowRepository by lazy {
        FollowRepositoryImpl(
            FollowRemoteDataSourceImpl()
        )
    }
    val customId = GlobalApplication.prefs.getString("custom_id", "empty")
    val followers = MutableLiveData<List<FUser>>()

    fun callFollower() {
        //프로필 세팅
        if (customId !== "empty") {
            followRepository.getFollower(customId, success = {
                val users = arrayListOf<FUser>()
                for (follower in it) {
                    follower.follower?.let{
                        f -> users.add(f)
                    }
                }

                followers.value = users
            }, failed = { Log.e("error", it) })
        }
    }
}
