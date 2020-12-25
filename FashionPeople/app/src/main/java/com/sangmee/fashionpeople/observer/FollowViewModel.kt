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

class FollowViewModel : ViewModel() {
    private val followRepository: FollowRepository by lazy {
        FollowRepositoryImpl(
            FollowRemoteDataSourceImpl()
        )
    }
    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    val customId = GlobalApplication.prefs.getString("${loginType}_custom_id", "empty")
    val followers = MutableLiveData<List<FUser>>()
    val followings = MutableLiveData<List<FUser>>()
    //팔로워의 팔로잉 여부
    val isFollowingsFollower = MutableLiveData<MutableMap<String, Boolean>>()
    //팔로잉의 팔로잉 여부
    val isFollowingsFollowing = MutableLiveData<MutableMap<String, Boolean>>()
    val callActivity = SingleLiveEvent<String>()

    fun callFollower(userId: String) {
        followRepository.getFollower(userId, success = {
            val users = arrayListOf<FUser>()
            for (follower in it) {
                follower.follower?.let { f ->
                    users.add(f)
                }
            }
            followers.value = users
        }, failed = { Log.e("error", it) })

    }

    fun callFollowing(userId: String) {
        followRepository.getFollowing(userId, success = {
            val users = arrayListOf<FUser>()
            for (following in it) {
                following.following?.let { f ->
                    users.add(f)
                }
            }
            followings.value = users
        }, failed = { Log.e("error", it) })

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

    fun callOtherActivity(customId: String) {
        callActivity.value = customId
    }
}
