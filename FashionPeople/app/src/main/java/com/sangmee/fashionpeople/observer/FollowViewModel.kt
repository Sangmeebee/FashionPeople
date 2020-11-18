package com.sangmee.fashionpeople.observer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FollowRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.repository.FollowRepository
import com.sangmee.fashionpeople.data.repository.FollowRepositoryImpl

class FollowViewModel : ViewModel() {
    private val followRepository: FollowRepository by lazy {
        FollowRepositoryImpl(
            FollowRemoteDataSourceImpl()
        )
    }
    val customId = GlobalApplication.prefs.getString("custom_id", "empty")
    val followers = MutableLiveData<List<FUser>>()
    val followings = MutableLiveData<List<FUser>>()
    val isFollowingsFollower = MutableLiveData<MutableMap<String, Boolean>>()
    val isFollowingsFollowing = MutableLiveData<MutableMap<String, Boolean>>()

    fun callFollower() {
        //프로필 세팅
        if (customId !== "empty") {
            followRepository.getFollower(customId, success = {
                val users = arrayListOf<FUser>()
                val isFollowingMap = mutableMapOf<String, Boolean>()
                for (follower in it) {
                    follower.follower?.let { f ->
                        users.add(f)
                    }
                    follower.follower!!.id?.let { id ->
                        follower.isFollowing?.let { isFollowing ->
                            isFollowingMap[id] = isFollowing
                        }
                    }
                }
                followers.value = users
                isFollowingsFollower.value = isFollowingMap
                Log.d("sangmin", isFollowingsFollower.value.toString())
            }, failed = { Log.e("error", it) })
        }
    }

    fun callFollowing() {
        //프로필 세팅
        if (customId !== "empty") {
            followRepository.getFollowing(customId, success = {
                val users = arrayListOf<FUser>()
                val isFollowingMap = mutableMapOf<String, Boolean>()
                for (following in it) {
                    following.following?.let { f ->
                        users.add(f)
                    }
                    following.following!!.id?.let { id ->
                        isFollowingMap[id] = true
                    }
                }
                followings.value = users
                isFollowingsFollowing.value = isFollowingMap
            }, failed = { Log.e("error", it) })
        }
    }
}
