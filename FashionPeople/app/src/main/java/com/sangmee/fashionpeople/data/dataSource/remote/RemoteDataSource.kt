package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.FUser

interface RemoteDataSource {
    fun getAllFUser(
        success: (List<FUser>) -> Unit,
        failed: (String) -> Unit
    )

    fun getFUser(
        id: String,
        success: (FUser) -> Unit,
        failed: (String) -> Unit
    )

    fun addUser(
        user: FUser,
        success: (String) -> Unit,
        failed: (String) -> Unit
    )
}
