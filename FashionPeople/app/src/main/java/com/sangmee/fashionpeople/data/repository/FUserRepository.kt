package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.model.FUser
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Path

interface FUserRepository {
    fun getAllFUser(
        success: (List<FUser>) -> Unit,
        failed: (String) -> Unit
    )

    fun getFUser(@Path("id") id: String): Single<FUser>

    fun getIsEigenvalue(@Path("nickName") nickName: String): Single<Boolean>

    fun getSearchUser(@Path("nickName") nickName: String): Single<List<FUser>>

    fun addUser(
        user: FUser,
        success: () -> Unit,
        failed: (String) -> Unit
    )

    fun updateUser(id: String, user: FUser): Completable

    fun deleteUser(id: String): Completable
}
