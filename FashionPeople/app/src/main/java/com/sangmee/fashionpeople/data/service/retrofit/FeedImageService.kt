package com.sangmee.fashionpeople.data.service.retrofit

import com.sangmee.fashionpeople.data.model.Evaluation
import com.sangmee.fashionpeople.data.model.FeedImage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.*

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
    fun getFeedImages(@Path("id") id: String): Single<List<FeedImage>>

    @GET("feedImage/evaluated/{id}")
    fun getEvaluatedFeedImage(@Path("id") id: String): Call<FeedImage>

    @POST("feedImage/{id}")
    fun postFeedImage(@Path("id") id: String, @Body feedImage: FeedImage): Call<FeedImage>

    @GET("feedImage/following/{userId}")
    fun getFollowingFeedImages(@Path("userId") id: String): Single<List<FeedImage>>

}
