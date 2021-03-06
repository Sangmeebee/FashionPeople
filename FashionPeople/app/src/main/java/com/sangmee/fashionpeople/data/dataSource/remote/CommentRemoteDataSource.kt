package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.Comment
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.DELETE
import retrofit2.http.Path

interface CommentRemoteDataSource {

    fun getImageComments(imageName: String): Single<List<Comment>>

    fun updateImageComment(userId: String, imageName: String, comment: Comment): Completable

    fun deleteImageComment(@Path("id") id: Int): Completable
}
