package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.data.service.retrofit.RetrofitClient
import io.reactivex.Completable
import io.reactivex.Single

class CommentRemoteDataSourceImpl : CommentRemoteDataSource {
    override fun getImageComment(imageName: String): Single<List<Comment>> {
        return RetrofitClient.getCommentService().getImageComment(imageName)
    }

    override fun updateImageComment(imageName: String, comment: Comment): Completable {
        return RetrofitClient.getCommentService().updateImageComment(imageName, comment)
    }

}