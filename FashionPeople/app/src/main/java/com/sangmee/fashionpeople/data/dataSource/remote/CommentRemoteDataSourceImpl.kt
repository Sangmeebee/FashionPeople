package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.data.service.retrofit.RetrofitClient
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class CommentRemoteDataSourceImpl : CommentRemoteDataSource {
    override fun getImageComments(imageName: String): Single<List<Comment>> {
        return RetrofitClient.getCommentService().getImageComments(imageName)
    }

    override fun updateImageComment(
        userId: String,
        imageName: String,
        comment: Comment
    ): Completable {
        return RetrofitClient.getCommentService().updateImageComment(userId, imageName, comment)
    }

}
