package com.sangmee.fashionpeople.data.dataSource.local

interface SearchLocalDataSource {

    fun saveRecentSearchQuery(query: String)
    fun readRecentSearchQuery(): ArrayList<String>
    fun deleteRecentSearchQuery(query: String)
    fun clearRecentSearchQuery()
}
