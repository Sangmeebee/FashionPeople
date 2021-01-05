package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.model.Follower
import com.sangmee.fashionpeople.data.model.Following
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Path

interface FollowRepository {

    fun getFollower(@Path("userId") userId: String): Single<List<FUser>>

    fun getFollowing(@Path("userId") userId: String): Single<List<FUser>>

    fun getIsFollowing(userId: String, customId:String, success: (Boolean) -> Unit, failed: (String) -> Unit)

    fun getIsFollowingsFollowing(
        userId: String,
        customId: String,
        success: (Map<String, Boolean>) -> Unit,
        failed: (String) -> Unit
    )

    fun getIsFollowingsFollower(
        userId: String,
        customId: String,
        success: (Map<String, Boolean>) -> Unit,
        failed: (String) -> Unit
    )

    fun updateFollowing(
        userId: String,
        followingId: String,
        success: () -> Unit,
        failed: (String) -> Unit
    )

    fun deleteFollowing(
        userId: String,
        followingId: String,
        success: () -> Unit,
        failed: (String) -> Unit
    )
}
