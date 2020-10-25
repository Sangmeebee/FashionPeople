package com.sangmee.fashionpeople.retrofit.service

import com.sangmee.fashionpeople.retrofit.model.FeedImage
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface FeedImageService {

    @GET("feedImage/")
    fun getAllFeedImages(): Single<List<FeedImage>>

    @GET("feedImage/{id}")
    fun getFeedImages(@Path("id") id: String): Call<List<FeedImage>>

    @PUT("feedImage/{id}")
    fun putFeedImage(@Path("id") id: String, @Body feedImage: FeedImage): Call<FeedImage>
}
