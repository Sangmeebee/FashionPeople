package com.sangmee.fashionpeople.retrofit

import retrofit2.Call
import retrofit2.Response


class CustomerManager() {
    var call = RetrofitClient().getUsersRetrofit()

    fun onResponse(call: Call<FUser>, response: Response<FUser>) {
        if (response.isSuccessful()) { //check for Response status
            val result: FUser? = response.body() //리스폰의 바디를 Result객체로 담아쥼.
            result?.id
            result?.name
            result?.instagramId
            //Todo : 성공시 할일
        } else {
            // Todo: 실패시 할일
        }
    }

}