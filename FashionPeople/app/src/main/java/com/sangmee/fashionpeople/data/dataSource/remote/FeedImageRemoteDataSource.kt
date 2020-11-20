package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.Evaluation
import com.sangmee.fashionpeople.data.model.FeedImage
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Path

interface FeedImageRemoteDataSource {

    fun getAllFeedImages(): Single<List<FeedImage>>

    fun getFeedImageByName(@Path("imageName") imageName: String): Single<FeedImage>

    fun getOtherImages(@Path("id") id: String): Single<List<FeedImage>>

    fun updateImageScore(@Path("imageName") imageName: String, @Body evaluation: Evaluation): Completable

    fun getFeedImages(id: String, success: (List<FeedImage>) -> Unit, failed: (String) -> Unit)

    fun postFeedImage(@Path("id") id: String, @Body feedImage: FeedImage): Call<FeedImage>

    fun getFollowingFeedImages(@Path("userId") id: String): Single<List<FeedImage>>
}

