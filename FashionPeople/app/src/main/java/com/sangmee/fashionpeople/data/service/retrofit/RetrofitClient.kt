package com.sangmee.fashionpeople.data.service.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "http://54.180.224.29:3333/api/v1/"

object RetrofitClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    fun getFUserService(): FUserService = retrofit.create(FUserService::class.java)
    fun getFeedImageService(): FeedImageService = retrofit.create(FeedImageService::class.java)
    fun getCommentService(): CommentService = retrofit.create(CommentService::class.java)
    fun getFollowerService(): FollowerService = retrofit.create(FollowerService::class.java)
    fun getFollowingService(): FollowingService = retrofit.create(FollowingService::class.java)
    fun getRankImageService(): RankImageService = retrofit.create(RankImageService::class.java)
    fun getSaveImageService(): SaveImageService = retrofit.create(SaveImageService::class.java)
    fun getBrandService(): BrandService = retrofit.create(BrandService::class.java)
}

