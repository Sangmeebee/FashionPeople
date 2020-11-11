package com.sangmee.fashionpeople.data.service.retrofit

import com.sangmee.fashionpeople.data.model.Follower
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface FollowerService {

    @GET("follower/{userId}")
    fun getFollower(@Path("userId") userId: String): Call<List<Follower>>

    @PUT("follower/{userId}")
    fun updateFollower(@Path("userId") userId: String): Call<Follower>
}
