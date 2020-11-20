package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.dataSource.remote.CommentRemoteDataSource
import com.sangmee.fashionpeople.data.model.Comment
import io.reactivex.Completable
import io.reactivex.Single

class CommentRepositoryImpl(
    private val commentRemoteDataSource: CommentRemoteDataSource
) : CommentRepository {

    override fun getImageComments(imageName: String): Single<List<Comment>> {
        return commentRemoteDataSource.getImageComments(imageName)
    }

    override fun updateImageComment(imageName: String, comment: Comment): Completable {
        return commentRemoteDataSource.updateImageComment(imageName, comment)
    }


}