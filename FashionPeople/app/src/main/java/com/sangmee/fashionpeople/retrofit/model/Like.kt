package com.sangmee.fashionpeople.retrofit.model


import com.google.gson.annotations.SerializedName

data class Like(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("imageId")
    val imageId: String?,
    @SerializedName("likePerson")
    val likePerson: String?
)