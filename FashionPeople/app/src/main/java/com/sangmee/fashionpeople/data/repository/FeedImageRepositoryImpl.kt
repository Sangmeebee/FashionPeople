package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSource
import com.sangmee.fashionpeople.data.model.Evaluation
import com.sangmee.fashionpeople.data.model.FeedImage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

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

    override fun getFeedImages(id: String): Single<List<FeedImage>> {
        return feedImageRemoteDataSource.getFeedImages(id)
    }

    override fun postFeedImage(id: String, feedImage: FeedImage): Completable {
        return feedImageRemoteDataSource.postFeedImage(id, feedImage)
    }

    override fun getFollowingFeedImages(id: String): Single<List<FeedImage>> {
        return feedImageRemoteDataSource.getFollowingFeedImages(id)
    }

    override fun getSearchScoreStyleImages(query: String): Single<List<FeedImage>> {
        return feedImageRemoteDataSource.getSearchScoreStyleImages(query)
    }

    override fun getSearchRecentStyleImages(query: String): Single<List<FeedImage>> {
        return feedImageRemoteDataSource.getSearchRecentStyleImages(query)
    }

    override fun getSearchScoreBrandImages(query: String): Single<List<FeedImage>> {
        return feedImageRemoteDataSource.getSearchScoreBrandImages(query)
    }

    override fun getSearchRecentBrandImages(query: String): Single<List<FeedImage>> {
        return feedImageRemoteDataSource.getSearchRecentBrandImages(query)
    }

    override fun deleteFeedImage(imageName: String): Completable {
        return feedImageRemoteDataSource.deleteFeedImage(imageName)
    }
}
