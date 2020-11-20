package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.model.Comment
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Path

interface CommentRepository {

    fun getImageComments(imageName: String): Single<List<Comment>>

    fun updateImageComment(userId: String, imageName: String, comment: Comment): Completable

}