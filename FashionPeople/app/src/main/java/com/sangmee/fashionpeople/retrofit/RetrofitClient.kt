package com.sangmee.fashionpeople.retrofit

import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import com.sangmee.fashionpeople.retrofit.service.FUserService
import com.sangmee.fashionpeople.retrofit.service.FeedImageService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "http://52.78.102.63:3333/api/v1/"

class RetrofitClient {
    val pref = GlobalApplication.prefs
    val customId = pref.getString("custom_id", "empty")

    private val fUserService: FUserService
    private val feedImageService: FeedImageService

    init {
        val retrofit = Retrofit.Builder()
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
        fUserService = retrofit.create(FUserService::class.java)
        feedImageService = retrofit.create(FeedImageService::class.java)
    }

    fun getFUserService(): FUserService = fUserService
    fun getFeedImageService(): FeedImageService = feedImageService

}
