package com.sangmee.fashionpeople.data.dataSource.local

interface SearchLocalDataSource {

    fun saveRecentSearchQuery(key: String, query: String)
    fun readRecentSearchQuery(key: String): ArrayList<String>
    fun deleteRecentSearchQuery(key: String, query: String)
    fun clearRecentSearchQuery(key: String)
}
