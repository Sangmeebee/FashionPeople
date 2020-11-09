package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.model.FUser

interface Repository {
    fun getAllFUser(
        success: (List<FUser>) -> Unit,
        failed: (String) -> Unit
    )

    fun getFUser(
        id: String,
        success: (FUser) -> Unit,
        failed: (String) -> Unit
    )
}
