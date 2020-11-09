package com.sangmee.fashionpeople.retrofit.service

import com.sangmee.fashionpeople.retrofit.model.FUser
import retrofit2.Call
import retrofit2.http.*

interface FUserService {


    @GET("users")
    fun getAllFUser(): Call<List<FUser>>

    @GET("users/{id}")
    fun getFUser(@Path("id") id: String): Call<FUser>

    @POST("users")
    fun addUser(@Body body: FUser): Call<FUser>


    @PUT("users/{id}")
    fun updateUserById(
        @Path("id") id: String,
        @Body body: FUser
    ): Call<FUser>

}
