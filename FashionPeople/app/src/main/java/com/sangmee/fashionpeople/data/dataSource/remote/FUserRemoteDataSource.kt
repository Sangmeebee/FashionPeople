package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.FUser
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface FUserRemoteDataSource {
    fun getAllFUser(
        success: (List<FUser>) -> Unit,
        failed: (String) -> Unit
    )

    fun getFUser(id: String): Single<FUser>

    fun addUser(
        user: FUser,
        success: () -> Unit,
        failed: (String) -> Unit
    )

    fun updateUser(id: String, user: FUser): Completable

    fun deleteUser(id: String): Completable
}
