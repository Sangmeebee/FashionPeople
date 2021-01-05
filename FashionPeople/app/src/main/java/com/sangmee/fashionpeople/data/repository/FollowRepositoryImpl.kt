package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.dataSource.remote.FollowRemoteDataSource
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.model.Follower
import com.sangmee.fashionpeople.data.model.Following
import io.reactivex.rxjava3.core.Single

class FollowRepositoryImpl(private val followRemoteDataSource: FollowRemoteDataSource) :
    FollowRepository {

    override fun getFollower(userId: String): Single<List<FUser>> {
        return followRemoteDataSource.getFollower(userId)
    }

    override fun getFollowing(userId: String): Single<List<FUser>> {
        return followRemoteDataSource.getFollowing(userId)
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
