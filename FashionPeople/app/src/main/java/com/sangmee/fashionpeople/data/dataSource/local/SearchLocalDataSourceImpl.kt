package com.sangmee.fashionpeople.data.dataSource.local

import com.sangmee.fashionpeople.data.GlobalApplication

class SearchLocalDataSourceImpl : SearchLocalDataSource {

    override fun saveRecentSearchQuery(query: String) {
        GlobalApplication.prefs.saveRecentSearchQuery(query)
    }

    override fun readRecentSearchQuery(): ArrayList<String> {
        val temp = arrayListOf<String>()
        val dataList = GlobalApplication.prefs.readRecentSearchQuery()
        for (i in dataList.size - 1 downTo 0) {
            temp.add(dataList[i])
        }
        return temp
    }

    override fun deleteRecentSearchQuery(query: String) {
        GlobalApplication.prefs.deleteRecentSearchQuery(query)
    }

    override fun clearRecentSearchQuery() {
        GlobalApplication.prefs.clearRecentSearchQuery()
    }
}
