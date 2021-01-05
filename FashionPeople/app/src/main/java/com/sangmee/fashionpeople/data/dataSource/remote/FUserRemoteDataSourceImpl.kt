package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.service.retrofit.RetrofitClient
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FUserRemoteDataSourceImpl : FUserRemoteDataSource {

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

    override fun getFUser(id: String): Single<FUser> {
        return RetrofitClient.getFUserService().getFUser(id)
    }

    override fun addUser(user: FUser, success: () -> Unit, failed: (String) -> Unit) {
        RetrofitClient.getFUserService().addUser(user).enqueue(object : Callback<FUser> {
            override fun onFailure(call: retrofit2.Call<FUser>, t: Throwable) {
                failed(t.message.toString())
            }

            override fun onResponse(call: Call<FUser>, response: Response<FUser>) {
                success()
            }
        })
    }

    override fun updateUser(id: String, user: FUser): Completable {
        return RetrofitClient.getFUserService().updateUserById(id, user)
    }

    override fun deleteUser(id: String): Completable {
        return RetrofitClient.getFUserService().deleteUser(id)
    }
}
