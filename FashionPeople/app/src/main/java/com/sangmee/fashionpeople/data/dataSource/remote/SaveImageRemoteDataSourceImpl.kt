package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.service.retrofit.RetrofitClient
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class SaveImageRemoteDataSourceImpl : SaveImageRemoteDataSource {

    override fun getSaveImages(userId: String): Single<List<FeedImage>> {
        return RetrofitClient.getSaveImageService().getSaveImages(userId)
    }

    override fun postSaveImage(userId: String, imageName: String): Completable {
        return RetrofitClient.getSaveImageService().postSaveImage(userId, imageName)
    }

    override fun deleteSaveImage(userId: String, imageName: String): Completable {
        return RetrofitClient.getSaveImageService().deleteSaveImage(userId, imageName)
    }
}
