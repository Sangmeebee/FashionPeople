package com.sangmee.fashionpeople.retrofit.model


import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("comments")
    val comments: List<Comment>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageName")
    val imageName: String,
    @SerializedName("likes")
    val likes: List<Like>,
    @SerializedName("timeStamp")
    val timeStamp: String
)