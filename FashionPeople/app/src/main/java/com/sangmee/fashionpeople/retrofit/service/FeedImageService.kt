package com.sangmee.fashionpeople.retrofit.service

import com.sangmee.fashionpeople.retrofit.model.FeedImage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FeedImageService {

    @GET("/feedImage/{id}")
    fun getFeedImage(@Path("id") id: String): Call<FeedImage>
}