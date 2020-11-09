package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.service.retrofit.RetrofitClient
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSourceImpl : RemoteDataSource {

    override fun getAllFUser(success: (List<FUser>) -> Unit, failed: (String) -> Unit) {
        RetrofitClient.getFUserService().getAllFUser().enqueue(object :
            Callback<List<FUser>> {
            override fun onFailure(call: retrofit2.Call<List<FUser>>, t: Throwable) {
                failed(t.message.toString())
            }

            override fun onResponse(
                call: retrofit2.Call<List<FUser>>,
                response: Response<List<FUser>>
            ) {
                if (response.isSuccessful) {
                    val res = response.body()
                    res?.let {
                        success(it)
                    }
                } else {
                    failed(response.message())
                }
            }
        })
    }

    override fun getFUser(id: String, success: (FUser) -> Unit, failed: (String) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun addUser(user: FUser, success: (String) -> Unit, failed: (String) -> Unit) {
        TODO("Not yet implemented")
    }
}
