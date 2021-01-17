package com.sangmee.fashionpeople.data.dataSource.local

import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.model.FUser

class SearchLocalDataSourceImpl : SearchLocalDataSource {

    override fun saveRecentSearchQuery(key: String, query: String) {
        GlobalApplication.prefs.saveRecentSearchQuery(key, query)
    }

    override fun readRecentSearchQuery(key: String): ArrayList<String> {
        val temp = arrayListOf<String>()
        val dataList = GlobalApplication.prefs.readRecentSearchQuery(key)
        for (i in dataList.size - 1 downTo 0) {
            temp.add(dataList[i])
        }
        return temp
    }

    override fun deleteRecentSearchQuery(key: String, query: String) {
        GlobalApplication.prefs.deleteRecentSearchQuery(key, query)
    }

    override fun clearRecentSearchQuery(key: String) {
        GlobalApplication.prefs.clearRecentSearchQuery(key)
    }

    override fun saveRecentSearchUser(key: String, user: FUser) {
        GlobalApplication.prefs.saveRecentSearchUser(key, user)
    }

    override fun readRecentSearchUser(key: String): ArrayList<FUser> {
        val temp = arrayListOf<FUser>()
        val dataList = GlobalApplication.prefs.readRecentSearchUser(key)
        for (i in dataList.size - 1 downTo 0) {
            temp.add(dataList[i])
        }
        return temp
    }

    override fun deleteRecentSearchUser(key: String, user: FUser) {
        GlobalApplication.prefs.deleteRecentSearchUser(key, user)
    }

    override fun clearRecentSearchUser(key: String) {
        GlobalApplication.prefs.clearRecentSearchUser(key)
    }
}
