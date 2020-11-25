package com.sangmee.fashionpeople.data.service.retrofit

import com.sangmee.fashionpeople.data.model.Following
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface FollowingService {

    @GET("following/{userId}")
    fun getFollowing(@Path("userId") userId: String): Call<List<Following>>

    @GET("following/{userId}/{customId}")
    fun getIsFollowing(@Path("userId") userId: String, @Path("customId") customId: String): Call<Boolean>

    @GET("following/isFollowingsFollowing/{userId}/{customId}")
    fun getIsFollowingsFollowing(@Path("userId") userId: String, @Path("customId") customId: String): Call<Map<String, Boolean>>

    @GET("following/isFollowingsFollower/{userId}/{customId}")
    fun getIsFollowingsFollower(@Path("userId") userId: String, @Path("customId") customId: String): Call<Map<String, Boolean>>

    @PUT("following/{userId}/{followingId}")
    fun updateFollowing(
        @Path("userId") userId: String,
        @Path("followingId") followingId: String
    ): Call<Following>

    @DELETE("following/{userId}/{followingId}")
    fun deleteFollowing(
        @Path("userId") userId: String,
        @Path("followingId") followingId: String
    ): Call<Unit>
}
