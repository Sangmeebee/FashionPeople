package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.Evaluation
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.service.retrofit.RetrofitClient
import io.reactivex.Completable
import io.reactivex.Single
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

    override fun getFeedImages(
        id: String,
        success: (List<FeedImage>) -> Unit,
        failed: (String) -> Unit
    ) {
        RetrofitClient.getFeedImageService().getFeedImages(id)
            .enqueue(object : Callback<List<FeedImage>> {
                override fun onResponse(
                    call: Call<List<FeedImage>>,
                    response: Response<List<FeedImage>>
                ) {
                    response.body()?.let {
                        success(it)
                    }
                }

                override fun onFailure(call: Call<List<FeedImage>>, t: Throwable) {
                    failed(t.message.toString())
                }
            })
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
