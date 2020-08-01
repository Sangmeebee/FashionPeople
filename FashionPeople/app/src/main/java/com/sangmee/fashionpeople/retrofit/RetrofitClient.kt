package com.sangmee.fashionpeople.retrofit

import com.google.gson.GsonBuilder
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    val pref = GlobalApplication.prefs
    val customId = pref.getString("custom_id", "empty")

    private val fashionPeopleService: FashionPeopleService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        fashionPeopleService = retrofit.create(FashionPeopleService::class.java)
    }

    fun getInstance(): FashionPeopleService = fashionPeopleService
}