package com.sangmee.fashionpeople.data.service.retrofit

import com.sangmee.fashionpeople.data.model.Evaluation
import com.sangmee.fashionpeople.data.model.FeedImage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface FeedImageService {

    @GET("feedImage/")
    fun getAllFeedImages(): Single<List<FeedImage>>

    @GET("feedImage/imageName/{imageName}")
    fun getFeedImageByName(@Path("imageName") imageName: String): Single<FeedImage>

    @GET("feedImage/other/{id}")
    fun getOtherImages(@Path("id") id: String): Single<List<FeedImage>>

    @PUT("feedImage/evaluation/{imageName}")
    fun updateImageScore(
        @Path("imageName") imageName: String,
        @Body evaluation: Evaluation
    ): Completable

    @GET("feedImage/{id}")
    fun getFeedImages(@Path("id") id: String): Single<List<FeedImage>>

    @POST("feedImage/{id}")
    fun postFeedImage(@Path("id") id: String, @Body feedImage: FeedImage): Completable

    @GET("feedImage/following/{userId}")
    fun getFollowingFeedImages(@Path("userId") id: String): Single<List<FeedImage>>

    @GET("feedImage/search/style/score/{query}")
    fun getSearchScoreStyleImages(@Path("query") query: String): Single<List<FeedImage>>

    @GET("feedImage/search/style/recent/{query}")
    fun getSearchRecentStyleImages(@Path("query") query: String): Single<List<FeedImage>>

    @GET("feedImage/search/brand/score/{query}")
    fun getSearchScoreBrandImages(@Path("query") query: String): Single<List<FeedImage>>

    @GET("feedImage/search/brand/recent/{query}")
    fun getSearchRecentBrandImages(@Path("query") query: String): Single<List<FeedImage>>

    @DELETE("feedImage/{imageName}")
    fun deleteFeedImage(@Path("imageName") imageName: String): Completable

}
