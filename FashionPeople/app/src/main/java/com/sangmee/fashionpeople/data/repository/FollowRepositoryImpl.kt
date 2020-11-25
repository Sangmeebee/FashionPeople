package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.dataSource.remote.FollowRemoteDataSource
import com.sangmee.fashionpeople.data.model.Follower
import com.sangmee.fashionpeople.data.model.Following

class FollowRepositoryImpl(private val followRemoteDataSource: FollowRemoteDataSource) :
    FollowRepository {
    override fun getFollower(
        userId: String,
        success: (List<Follower>) -> Unit,
        failed: (String) -> Unit
    ) {
        followRemoteDataSource.getFollower(userId, success, failed)
    }

    override fun getFollowing(
        userId: String,
        success: (List<Following>) -> Unit,
        failed: (String) -> Unit
    ) {
        followRemoteDataSource.getFollowing(userId, success, failed)
    }

    override fun updateFollowing(
        userId: String,
        followingId: String,
        success: () -> Unit,
        failed: (String) -> Unit
    ) {
        followRemoteDataSource.updateFollowing(userId, followingId, success, failed)
    }

    override fun deleteFollowing(
        userId: String,
        followingId: String,
        success: () -> Unit,
        failed: (String) -> Unit
    ) {
        followRemoteDataSource.deleteFollowing(userId, followingId, success, failed)
    }

    override fun getIsFollowing(
        userId: String,
        customId: String,
        success: (Boolean) -> Unit,
        failed: (String) -> Unit
    ) {
        followRemoteDataSource.getIsFollowing(userId, customId, success, failed)
    }

    override fun getIsFollowingsFollowing(
        userId: String,
        customId: String,
        success: (Map<String, Boolean>) -> Unit,
        failed: (String) -> Unit
    ) {
        followRemoteDataSource.getIsFollowingsFollowing(userId, customId, success, failed)
    }

    override fun getIsFollowingsFollower(
        userId: String,
        customId: String,
        success: (Map<String, Boolean>) -> Unit,
        failed: (String) -> Unit
    ) {
        followRemoteDataSource.getIsFollowingsFollower(userId, customId, success, failed)
    }
}
