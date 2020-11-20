package com.sangmee.fashionpeople.data.service.retrofit

import com.sangmee.fashionpeople.data.model.Comment
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface CommentService {

    @GET("feedImage/comment/{image_name}")
    fun getImageComments(@Path("image_name") imageName: String): Single<List<Comment>>

    @PUT("feedImage/comment/{userId}/{imageName}")
    fun updateImageComment(
        @Path("userId") userId: String,
        @Path("imageName") imageName: String,
        @Body comment: Comment
    ): Completable

}