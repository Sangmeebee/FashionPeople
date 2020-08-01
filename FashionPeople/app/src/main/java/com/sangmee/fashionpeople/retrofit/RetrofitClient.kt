package com.sangmee.fashionpeople.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    private val fashionPeopleService: FashionPeopleService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        fashionPeopleService = retrofit.create(FashionPeopleService::class.java)
    }
    fun getUsersRetrofit() : Call<FUser> {
        return fashionPeopleService.getFUser("apfhdznzl")
    }
}