package com.sangmee.fashionpeople.data.service.retrofit

import com.sangmee.fashionpeople.data.model.FeedImage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface SaveImageService {

    @GET("saveImage/{userId}")
    fun getSaveImages(@Path("userId") userId: String) : Single<List<FeedImage>>

    @POST("saveImage/{userId}/{imageName}")
    fun postSaveImage(@Path("userId") userId: String, @Path("imageName") imageName: String) : Completable

    @DELETE("saveImage/{userId}/{imageName}")
    fun deleteSaveImage(@Path("userId") userId: String, @Path("imageName") imageName: String) : Completable
}
