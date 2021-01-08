package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.model.FeedImage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.Path

interface SaveImageRepository {

    fun getSaveImages(@Path("userId") userId: String): Single<List<FeedImage>>

    fun postSaveImage(
        @Path("userId") userId: String,
        @Path("imageName") imageName: String
    ): Completable

    fun deleteSaveImage(
        @Path("userId") userId: String,
        @Path("imageName") imageName: String
    ): Completable

}
