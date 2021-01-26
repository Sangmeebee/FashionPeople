package com.sangmee.fashionpeople.data.service.retrofit

import com.sangmee.fashionpeople.data.model.Comment
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface CommentService {

    @GET("feedImage/comment/{image_name}")
    fun getImageComments(@Path("image_name") imageName: String): Single<List<Comment>>

    @Headers(
        "Accept:application/json, text/plain, */*",
        "Content-Type:application/json;charset=UTF-8"
    )
    @PUT("feedImage/comment/{userId}/{imageName}")
    fun updateImageComment(
        @Path("userId") userId: String,
        @Path("imageName") imageName: String,
        @Body comment: Comment
    ): Completable

    @DELETE("feedImage/comment/{id}")
    fun deleteImageComment(@Path("id") id: Int): Completable
}
