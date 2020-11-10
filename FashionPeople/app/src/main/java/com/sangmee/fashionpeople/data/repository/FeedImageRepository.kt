package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.model.Evaluation
import com.sangmee.fashionpeople.data.model.FeedImage
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface FeedImageRepository {

    fun getAllFeedImages(): Single<List<FeedImage>>

    fun getFeedImageByName(@Path("imageName") imageName: String): Single<FeedImage>

    fun getOtherImages(@Path("id") id: String): Single<List<FeedImage>>

    fun updateImageScore(@Path("imageName") imageName: String, @Body evaluation: Evaluation): Completable

    fun getFeedImages(@Path("id") id: String): Call<List<FeedImage>>

    fun postFeedImage(@Path("id") id: String, @Body feedImage: FeedImage): Call<FeedImage>
}