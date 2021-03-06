package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.Evaluation
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.service.retrofit.RetrofitClient
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedImageRemoteDataSourceImpl : FeedImageRemoteDataSource {

    override fun getAllFeedImages(): Single<List<FeedImage>> {
        return RetrofitClient.getFeedImageService().getAllFeedImages()
    }

    override fun getFeedImageByName(imageName: String): Single<FeedImage> {
        return RetrofitClient.getFeedImageService().getFeedImageByName(imageName)
    }


    override fun getFeedImages(id: String): Single<List<FeedImage>> {
        return RetrofitClient.getFeedImageService().getFeedImages(id)
    }

    override fun getOtherImages(id: String): Single<List<FeedImage>> {
        return RetrofitClient.getFeedImageService().getOtherImages(id)
    }

    override fun postFeedImage(id: String, feedImage: FeedImage): Completable {
        return RetrofitClient.getFeedImageService().postFeedImage(id, feedImage)
    }

    override fun updateImageScore(imageName: String, evaluation: Evaluation): Completable {
        return RetrofitClient.getFeedImageService().updateImageScore(imageName, evaluation)
    }

    override fun getFollowingFeedImages(id: String): Single<List<FeedImage>> {
        return RetrofitClient.getFeedImageService().getFollowingFeedImages(id)
    }

    override fun getSearchScoreStyleImages(query: String): Single<List<FeedImage>> {
        return RetrofitClient.getFeedImageService().getSearchScoreStyleImages(query)
    }

    override fun getSearchRecentStyleImages(query: String): Single<List<FeedImage>> {
        return RetrofitClient.getFeedImageService().getSearchRecentStyleImages(query)
    }

    override fun getSearchScoreBrandImages(query: String): Single<List<FeedImage>> {
        return RetrofitClient.getFeedImageService().getSearchScoreBrandImages(query)
    }

    override fun getSearchRecentBrandImages(query: String): Single<List<FeedImage>> {
        return RetrofitClient.getFeedImageService().getSearchRecentBrandImages(query)
    }

    override fun deleteFeedImage(imageName: String): Completable {
        return RetrofitClient.getFeedImageService().deleteFeedImage(imageName)
    }
}
