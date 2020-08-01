package com.sangmee.fashionpeople.retrofit

import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import com.sangmee.fashionpeople.kakaologin.PreferenceUtil
import retrofit2.Call
import retrofit2.http.*

const val BASE_URL = "http://52.78.102.63:3333/api/v1/"
val userId = GlobalApplication.prefs.getString("custom_id", "empty")

interface FashionPeopleService {


    @GET("users")
    fun getAllFUser(): Call<List<FUser>>

    @GET("users/{id}")
    fun getFUser(@Path("id") id: String): Call<FUser>

    @POST("users")
    fun addUser(@Body body: FUser): Call<FUser>

    @PUT("users/{id}")
    fun addUserById(
        @Path("id") id: String,
        @Body body: FUser
    ): Call<FUser>

}