package com.sangmee.fashionpeople.retrofit.model


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("timeStamp")
    val timeStamp: String?,
    @SerializedName("userId")
    val userId: String?
)