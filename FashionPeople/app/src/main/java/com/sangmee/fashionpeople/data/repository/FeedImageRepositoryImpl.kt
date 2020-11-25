package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSource
import com.sangmee.fashionpeople.data.model.Evaluation
import com.sangmee.fashionpeople.data.model.FeedImage
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Call

class FeedImageRepositoryImpl(
    private val feedImageRemoteDataSource: FeedImageRemoteDataSource
) : FeedImageRepository {

    override fun getAllFeedImages(): Single<List<FeedImage>> {
        return feedImageRemoteDataSource.getAllFeedImages()
    }

    override fun getFeedImageByName(imageName: String): Single<FeedImage> {
        return feedImageRemoteDataSource.getFeedImageByName(imageName)
    }

    override fun getOtherImages(id: String): Single<List<FeedImage>> {
        return feedImageRemoteDataSource.getOtherImages(id)
    }

    override fun updateImageScore(imageName: String, evaluation: Evaluation): Completable {
        return feedImageRemoteDataSource.updateImageScore(imageName, evaluation)
    }

    override fun getFeedImages(
        id: String,
        success: (List<FeedImage>) -> Unit,
        failed: (String) -> Unit
    ) {
        return feedImageRemoteDataSource.getFeedImages(id, success, failed)
    }

    override fun postFeedImage(id: String, feedImage: FeedImage): Call<FeedImage> {
        return feedImageRemoteDataSource.postFeedImage(id, feedImage)
    }

    override fun getFollowingFeedImages(id: String): Single<List<FeedImage>> {
        return feedImageRemoteDataSource.getFollowingFeedImages(id)
    }
}