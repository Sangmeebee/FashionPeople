package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.dataSource.remote.SaveImageRemoteDataSource
import com.sangmee.fashionpeople.data.model.FeedImage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class SaveImageRepositoryImpl(private val saveImageRemoteDataSource: SaveImageRemoteDataSource) :
    SaveImageRepository {

    override fun getSaveImages(userId: String): Single<List<FeedImage>> {
        return saveImageRemoteDataSource.getSaveImages(userId)
    }

    override fun postSaveImage(userId: String, imageName: String): Completable {
        return saveImageRemoteDataSource.postSaveImage(userId, imageName)
    }

    override fun deleteSaveImage(userId: String, imageName: String): Completable {
        return saveImageRemoteDataSource.deleteSaveImage(userId, imageName)
    }
}
