package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.Comment
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Path

interface CommentRemoteDataSource {

    fun getImageComments(@Path("image_name") imageName: String): Single<List<Comment>>

    fun updateImageComment(
        @Path("image_name") imageName: String,
        @Body comment: Comment
    ): Completable

}
