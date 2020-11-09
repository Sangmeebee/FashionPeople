package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.dataSource.local.LocalDataSource
import com.sangmee.fashionpeople.data.dataSource.remote.RemoteDataSource
import com.sangmee.fashionpeople.data.model.FUser

class RepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : Repository {
    override fun getAllFUser(success: (List<FUser>) -> Unit, failed: (String) -> Unit) {
        remoteDataSource.getAllFUser(success, failed)
    }
}
