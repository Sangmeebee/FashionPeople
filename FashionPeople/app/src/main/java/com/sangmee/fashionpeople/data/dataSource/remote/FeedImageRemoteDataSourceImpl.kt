package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.Evaluation
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.service.retrofit.RetrofitClient
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Call

class FeedImageRemoteDataSourceImpl : FeedImageRemoteDataSource {

    override fun getAllFeedImages(): Single<List<FeedImage>> {
        return RetrofitClient.getFeedImageService().getAllFeedImages()
    }

    override fun getFeedImageByName(imageName: String): Single<FeedImage> {
        return RetrofitClient.getFeedImageService().getFeedImageByName(imageName)
    }

    override fun getFeedImages(id: String): Call<List<FeedImage>> {
        return RetrofitClient.getFeedImageService().getFeedImages(id)
    }

    override fun getOtherImages(id: String): Single<List<FeedImage>> {
        return RetrofitClient.getFeedImageService().getOtherImages(id)
    }

    override fun postFeedImage(id: String, feedImage: FeedImage): Call<FeedImage> {
        return RetrofitClient.getFeedImageService().postFeedImage(id, feedImage)
    }

    override fun updateImageScore(imageName: String, evaluation: Evaluation): Completable {
        return RetrofitClient.getFeedImageService().updateImageScore(imageName, evaluation)
    }

    override fun getFollowingFeedImages(id: String): Single<List<FeedImage>> {
        return RetrofitClient.getFeedImageService().getFollowingFeedImages(id)
    }
}