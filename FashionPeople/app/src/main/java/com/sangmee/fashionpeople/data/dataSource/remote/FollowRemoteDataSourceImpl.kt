package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.Follower
import com.sangmee.fashionpeople.data.model.Following
import com.sangmee.fashionpeople.data.service.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowRemoteDataSourceImpl : FollowRemoteDataSource {
    override fun getFollower(
        userId: String,
        success: (List<Follower>) -> Unit,
        failed: (String) -> Unit
    ) {
        RetrofitClient.getFollowerService().getFollower(userId)
            .enqueue(object : Callback<List<Follower>> {
                override fun onResponse(
                    call: Call<List<Follower>>,
                    response: Response<List<Follower>>
                ) {
                    val res = response.body()
                    res?.let {
                        success(it)
                    }
                }

                override fun onFailure(call: Call<List<Follower>>, t: Throwable) {
                    failed(t.message.toString())
                }
            })
    }

    override fun getFollowing(
        userId: String,
        success: (List<Following>) -> Unit,
        failed: (String) -> Unit
    ) {
        RetrofitClient.getFollowingService().getFollowing(userId)
            .enqueue(object : Callback<List<Following>> {
                override fun onResponse(
                    call: Call<List<Following>>,
                    response: Response<List<Following>>
                ) {
                    val res = response.body()
                    res?.let {
                        success(it)
                    }
                }

                override fun onFailure(call: Call<List<Following>>, t: Throwable) {
                    failed(t.message.toString())
                }
            })
    }

    override fun updateFollowing(
        userId: String,
        followingId: String,
        success: () -> Unit,
        failed: (String) -> Unit
    ) {
        RetrofitClient.getFollowingService().updateFollowing(userId, followingId).enqueue(object :
            Callback<Following> {
            override fun onResponse(call: Call<Following>, response: Response<Following>) {
                success()
            }

            override fun onFailure(call: Call<Following>, t: Throwable) {
                failed(t.message.toString())
            }
        })
    }

    override fun deleteFollowing(
        userId: String,
        followingId: String,
        success: () -> Unit,
        failed: (String) -> Unit
    ) {
        RetrofitClient.getFollowingService().deleteFollowing(userId, followingId)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    success()
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    failed(t.message.toString())
                }
            })
    }

    override fun getIsFollowing(
        userId: String,
        customId: String,
        success: (Boolean) -> Unit,
        failed: (String) -> Unit
    ) {
        RetrofitClient.getFollowingService().getIsFollowing(userId, customId)
            .enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    val res = response.body()
                    res?.let {
                        success(it)
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    failed(t.message.toString())
                }
            })
    }

    override fun getIsFollowingsFollowing(
        userId: String,
        customId: String,
        success: (Map<String, Boolean>) -> Unit,
        failed: (String) -> Unit
    ) {
        RetrofitClient.getFollowingService().getIsFollowingsFollowing(userId, customId)
            .enqueue(object : Callback<Map<String, Boolean>> {
                override fun onResponse(
                    call: Call<Map<String, Boolean>>,
                    response: Response<Map<String, Boolean>>
                ) {
                    val res = response.body()
                    res?.let {
                        success(it)
                    }
                }

                override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
                    failed(t.message.toString())
                }
            })
    }

    override fun getIsFollowingsFollower(
        userId: String,
        customId: String,
        success: (Map<String, Boolean>) -> Unit,
        failed: (String) -> Unit
    ) {
        RetrofitClient.getFollowingService().getIsFollowingsFollower(userId, customId).enqueue(
            object : Callback<Map<String, Boolean>> {
                override fun onResponse(
                    call: Call<Map<String, Boolean>>,
                    response: Response<Map<String, Boolean>>
                ) {
                    val res = response.body()
                    res?.let {
                        success(it)
                    }
                }

                override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
                    failed(t.message.toString())
                }
            })
    }
}
