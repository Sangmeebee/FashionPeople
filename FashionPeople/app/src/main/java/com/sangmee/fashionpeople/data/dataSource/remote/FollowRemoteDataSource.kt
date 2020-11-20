package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.Follower
import com.sangmee.fashionpeople.data.model.Following

interface FollowRemoteDataSource {

    fun getFollower(userId: String, success: (List<Follower>) -> Unit, failed: (String) -> Unit)

    fun getFollowing(userId: String, success: (List<Following>) -> Unit, failed: (String) -> Unit)

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
