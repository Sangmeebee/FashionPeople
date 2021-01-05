package com.sangmee.fashionpeople.data.service.retrofit

import com.sangmee.fashionpeople.data.model.FUser
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.*

interface FUserService {


    @GET("users")
    fun getAllFUser(): Call<List<FUser>>

    @GET("users/{id}")
    fun getFUser(@Path("id") id: String): Single<FUser>

    @POST("users")
    fun addUser(@Body body: FUser): Call<FUser>

    @PUT("users/{id}")
    fun updateUserById(
        @Path("id") id: String,
        @Body body: FUser
    ): Completable

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") id: String): Completable

}
