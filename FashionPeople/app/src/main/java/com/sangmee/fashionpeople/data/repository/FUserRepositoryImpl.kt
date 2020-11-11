package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSource
import com.sangmee.fashionpeople.data.model.FUser

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
}
