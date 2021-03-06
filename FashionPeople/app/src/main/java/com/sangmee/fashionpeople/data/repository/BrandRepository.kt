package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.model.Brand
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.model.Style
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Path

interface BrandRepository {

    fun getStyle(@Path("style") style: String): Single<List<Style>>

    fun getBrand(@Path("brand") brand: String): Single<List<Brand>>

    fun putStyle(@Path("style") style: String): Completable

    fun putBrand(@Path("brand") brand: String): Completable

    fun getPopularStyle(): Single<List<Style>>

    fun getPopularBrand(): Single<List<Brand>>

    fun saveRecentSearchQuery(key: String, query: String)

    fun readRecentSearchQuery(key: String): ArrayList<String>

    fun deleteRecentSearchQuery(key: String, query: String)

    fun clearRecentSearchQuery(key: String)

    fun saveRecentSearchUser(key: String, user: FUser)

    fun readRecentSearchUser(key: String): ArrayList<FUser>

    fun deleteRecentSearchUser(key: String, user: FUser)

    fun clearRecentSearchUser(key: String)
}
