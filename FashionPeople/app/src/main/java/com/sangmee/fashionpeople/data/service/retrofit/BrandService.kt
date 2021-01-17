package com.sangmee.fashionpeople.data.service.retrofit

import com.sangmee.fashionpeople.data.model.Brand
import com.sangmee.fashionpeople.data.model.Style
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface BrandService {

    @GET("feedImage/style/{style}")
    fun getStyle(@Path("style") style: String): Single<List<Style>>

    @GET("feedImage/brand/{brand}")
    fun getBrand(@Path("brand") brand: String): Single<List<Brand>>

    @GET("feedImage/popularStyle")
    fun getPopularStyle(): Single<List<Style>>

    @GET("feedImage/popularBrand")
    fun getPopularBrand(): Single<List<Brand>>

    @PUT("feedImage/style/{style}")
    fun putStyle(@Path("style") style: String): Completable

    @PUT("feedImage/brand/{brand}")
    fun putBrand(@Path("brand") brand: String): Completable
}
