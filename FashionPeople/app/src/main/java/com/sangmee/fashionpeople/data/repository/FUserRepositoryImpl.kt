package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSource
import com.sangmee.fashionpeople.data.model.FUser
import io.reactivex.rxjava3.core.Completable

class FUserRepositoryImpl(
    private val FUserRemoteDataSource: FUserRemoteDataSource
) : FUserRepository {
    override fun getAllFUser(success: (List<FUser>) -> Unit, failed: (String) -> Unit) {
        FUserRemoteDataSource.getAllFUser(success, failed)
    }

    override fun getFUser(id: String, success: (FUser) -> Unit, failed: (String) -> Unit) {
        FUserRemoteDataSource.getFUser(id, success, failed)
    }

    override fun addUser(user: FUser, success: () -> Unit, failed: (String) -> Unit) {
        FUserRemoteDataSource.addUser(user, success, failed)
    }

    override fun updateUser(id: String, user: FUser): Completable {
        return FUserRemoteDataSource.updateUser(id, user)
    }

    override fun deleteUser(id: String): Completable {
        return FUserRemoteDataSource.deleteUser(id)
    }
}
