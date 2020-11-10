package com.sangmee.fashionpeople.data.service.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "http://15.165.254.24:3333/api/v1/"

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
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    fun getFUserService(): FUserService = retrofit.create(FUserService::class.java)
    fun getFeedImageService(): FeedImageService = retrofit.create(FeedImageService::class.java)
    fun getCommentService(): CommentService = retrofit.create(CommentService::class.java)
}

