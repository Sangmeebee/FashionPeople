package com.sangmee.fashionpeople.data.service.retrofit

import com.sangmee.fashionpeople.data.model.Following
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface FollowingService {

    @GET("following/{userId}")
    fun getFollower(@Path("userId") userId: String): Call<List<Following>>

    @PUT("following/{userId}/{followingId}")
    fun updateFollowing(
        @Path("userId") userId: String,
        @Path("followingId") followingId: String
    ): Call<Following>
}
