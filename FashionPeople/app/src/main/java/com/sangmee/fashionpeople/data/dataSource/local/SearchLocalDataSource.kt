package com.sangmee.fashionpeople.data.dataSource.local

import com.sangmee.fashionpeople.data.model.FUser

interface SearchLocalDataSource {

    fun saveRecentSearchQuery(key: String, query: String)
    fun readRecentSearchQuery(key: String): ArrayList<String>
    fun deleteRecentSearchQuery(key: String, query: String)
    fun clearRecentSearchQuery(key: String)

    fun saveRecentSearchUser(key: String, user: FUser)
    fun readRecentSearchUser(key: String): ArrayList<FUser>
    fun deleteRecentSearchUser(key: String, user: FUser)
    fun clearRecentSearchUser(key: String)
}
