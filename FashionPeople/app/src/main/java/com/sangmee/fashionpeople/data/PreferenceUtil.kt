package com.sangmee.fashionpeople.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("Fashionprefs", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }

    fun saveRecentSearchQuery(key: String, query: String) {
        val list = readRecentSearchQuery(key)
        if(query in list){
            list.remove(query)
        }
        list.add(query)
        if (list.size > 50) {
            list.removeAt(0)
        }
        val jsonArray = JSONArray(list)
        prefs.edit().putString(key, jsonArray.toString()).apply()
    }


    fun deleteRecentSearchQuery(key: String, query: String) {
        val list = readRecentSearchQuery(key)
        list.remove(query)
        val jsonArray = JSONArray(list)
        prefs.edit().putString(key, jsonArray.toString()).apply()
    }

    fun readRecentSearchQuery(key: String): ArrayList<String> {
        val json = prefs.getString(key, null)
        val dataList = arrayListOf<String>()

        json?.let {
            val jsonArray = JSONArray(it)
            for (i in 0 until jsonArray.length()) {
                dataList.add(jsonArray[i].toString())
            }
        }

        return dataList
    }

    fun clearRecentSearchQuery(key: String) {
        val list = readRecentSearchQuery(key)
        list.clear()
        val jsonArray = JSONArray(list)
        prefs.edit().putString(key, jsonArray.toString()).apply()
    }
}

