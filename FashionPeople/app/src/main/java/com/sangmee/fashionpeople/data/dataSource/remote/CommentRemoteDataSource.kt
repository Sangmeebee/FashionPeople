package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.Comment
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface CommentRemoteDataSource {

    fun getImageComments(imageName: String): Single<List<Comment>>

    fun updateImageComment(userId: String, imageName: String, comment: Comment): Completable

}
