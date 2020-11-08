package com.sangmee.fashionpeople.retrofit.service

import com.sangmee.fashionpeople.retrofit.model.Evaluation
import com.sangmee.fashionpeople.retrofit.model.FeedImage
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FeedImageService {

    @GET("feedImage/")
    fun getAllFeedImages(): Single<List<FeedImage>>

    @GET("feedImage/imageName/{imageName}")
    fun getFeedImageByName(@Path("imageName") imageName: String): Single<FeedImage>

    @GET("feedImage/other/{id}")
    fun getOtherImages(@Path("id") id: String): Single<List<FeedImage>>

    @PUT("feedImage/evaluation/{imageName}")
    fun updateImageScore(@Path("imageName") imageName: String, @Body evaluation: Evaluation): Completable

    @GET("feedImage/{id}")
    fun getFeedImages(@Path("id") id: String): Call<List<FeedImage>>

    @POST("feedImage/{id}")
    fun postFeedImage(@Path("id") id: String, @Body feedImage: FeedImage): Call<FeedImage>

}
