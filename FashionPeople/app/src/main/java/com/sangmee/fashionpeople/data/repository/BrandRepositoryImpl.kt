package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.dataSource.local.SearchLocalDataSource
import com.sangmee.fashionpeople.data.dataSource.remote.BrandRemoteDataSource
import com.sangmee.fashionpeople.data.model.Brand
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

    override fun saveRecentSearchQuery(query: String) {
        searchLocalDataSource.saveRecentSearchQuery(query)
    }

    override fun readRecentSearchQuery(): ArrayList<String> {
        return searchLocalDataSource.readRecentSearchQuery()
    }

    override fun deleteRecentSearchQuery(query: String) {
        searchLocalDataSource.deleteRecentSearchQuery(query)
    }

    override fun clearRecentSearchQuery() {
        searchLocalDataSource.clearRecentSearchQuery()
    }
}
