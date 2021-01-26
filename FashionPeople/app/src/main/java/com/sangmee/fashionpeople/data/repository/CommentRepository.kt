package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.model.Comment
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.DELETE
import retrofit2.http.Path

interface CommentRepository {

    fun getImageComments(imageName: String): Single<List<Comment>>

    fun updateImageComment(userId: String, imageName: String, comment: Comment): Completable

    fun deleteImageComment(@Path("id") id: Int): Completable
}
