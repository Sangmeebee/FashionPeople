package com.sangmee.fashionpeople.retrofit.service

import com.sangmee.fashionpeople.retrofit.model.FeedImage
import retrofit2.Call
import retrofit2.http.*

interface FeedImageService {

    @GET("feedImage/{id}")
    fun getFeedImage(@Path("id") id: String): Call<FeedImage>

    @PUT("feedImage/{id}")
    fun putFeedImage(@Path("id") id: String, @Body feedImage: FeedImage): Call<FeedImage>
}