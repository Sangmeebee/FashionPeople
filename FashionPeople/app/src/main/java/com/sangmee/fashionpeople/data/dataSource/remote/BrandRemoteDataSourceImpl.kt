package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.Brand
import com.sangmee.fashionpeople.data.model.Style
import com.sangmee.fashionpeople.data.service.retrofit.RetrofitClient
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class BrandRemoteDataSourceImpl : BrandRemoteDataSource {

    override fun getStyle(style: String): Single<List<Style>> {
        return RetrofitClient.getBrandService().getStyle(style)
    }

    override fun getBrand(brand: String): Single<List<Brand>> {
        return RetrofitClient.getBrandService().getBrand(brand)
    }

    override fun putStyle(style: String): Completable {
        return RetrofitClient.getBrandService().putStyle(style)
    }

    override fun putBrand(brand: String): Completable {
        return RetrofitClient.getBrandService().putBrand(brand)
    }

    override fun getPopularStyle(): Single<List<Style>> {
        return RetrofitClient.getBrandService().getPopularStyle()
    }

    override fun getPopularBrand(): Single<List<Brand>> {
        return RetrofitClient.getBrandService().getPopularBrand()
    }
}
