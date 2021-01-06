package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.FeedImage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Path

interface SaveImageRemoteDataSource {

    fun getSaveImages(@Path("userId") userId: String): Single<List<FeedImage>>

    fun postSaveImage(
        @Path("userId") userId: String,
        @Path("imageName") imageName: String
    ): Completable
}
