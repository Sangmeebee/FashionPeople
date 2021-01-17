package com.sangmee.fashionpeople.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sangmee.fashionpeople.data.model.FUser
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
        if (query in list) {
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

    fun saveRecentSearchUser(key: String, user: FUser) {
        val list = readRecentSearchUser(key)
        for (u in list) {
            if (u.id == user.id) {
                list.remove(u)
                break
            }
        }
        list.add(user)
        if (list.size > 20) {
            list.removeAt(0)
        }

        val gson = Gson()
        val json = gson.toJson(list)
        prefs.edit().putString(key, json).apply()
    }


    fun deleteRecentSearchUser(key: String, user: FUser) {
        val list = readRecentSearchUser(key)
        for (u in list) {
            if (u.id == user.id) {
                list.remove(u)
                break
            }
        }
        val gson = Gson()
        val json = gson.toJson(list)
        prefs.edit().putString(key, json).apply()
    }

    fun readRecentSearchUser(key: String): ArrayList<FUser> {
        val json = prefs.getString(key, null)
        var dataList = arrayListOf<FUser>()
        val gson = Gson()

        json?.let {
            val type = object : TypeToken<ArrayList<FUser>>() {}.type
            dataList = gson.fromJson(it, type)
        }
        Log.d("Sangmeebee", dataList.toString())
        return dataList
    }

    fun clearRecentSearchUser(key: String) {
        val list = readRecentSearchQuery(key)
        list.clear()
        val gson = Gson()
        val json = gson.toJson(list)
        prefs.edit().putString(key, json).apply()
    }
}

