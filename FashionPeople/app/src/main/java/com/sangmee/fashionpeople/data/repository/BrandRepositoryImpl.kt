package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.dataSource.local.SearchLocalDataSource
import com.sangmee.fashionpeople.data.dataSource.remote.BrandRemoteDataSource
import com.sangmee.fashionpeople.data.model.Brand
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.model.Style
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class BrandRepositoryImpl(
    private val brandRemoteDataSource: BrandRemoteDataSource,
    private val searchLocalDataSource: SearchLocalDataSource
) : BrandRepository {

    override fun getStyle(style: String): Single<List<Style>> {
        return brandRemoteDataSource.getStyle(style)
    }

    override fun getBrand(brand: String): Single<List<Brand>> {
        return brandRemoteDataSource.getBrand(brand)
    }

    override fun putStyle(style: String): Completable {
        return brandRemoteDataSource.putStyle(style)
    }

    override fun putBrand(brand: String): Completable {
        return brandRemoteDataSource.putBrand(brand)
    }

    override fun saveRecentSearchQuery(key: String, query: String) {
        searchLocalDataSource.saveRecentSearchQuery(key, query)
    }

    override fun readRecentSearchQuery(key: String): ArrayList<String> {
        return searchLocalDataSource.readRecentSearchQuery(key)
    }

    override fun deleteRecentSearchQuery(key: String, query: String) {
        searchLocalDataSource.deleteRecentSearchQuery(key, query)
    }

    override fun clearRecentSearchQuery(key: String) {
        searchLocalDataSource.clearRecentSearchQuery(key)
    }

    override fun saveRecentSearchUser(key: String, user: FUser) {
        searchLocalDataSource.saveRecentSearchUser(key, user)
    }

    override fun readRecentSearchUser(key: String): ArrayList<FUser> {
        return searchLocalDataSource.readRecentSearchUser(key)
    }

    override fun deleteRecentSearchUser(key: String, user: FUser) {
        searchLocalDataSource.deleteRecentSearchUser(key, user)
    }

    override fun clearRecentSearchUser(key: String) {
        searchLocalDataSource.clearRecentSearchUser(key)
    }
}
