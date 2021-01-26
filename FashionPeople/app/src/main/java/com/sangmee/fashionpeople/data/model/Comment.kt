package com.sangmee.fashionpeople.data.model


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("currentDateTime")
    val currentDateTime: String?,
    @SerializedName("image")
    val feedImage: FeedImage?,
    @SerializedName("user")
    val user: FUser?
) {
    constructor(content: String?) : this(
        null,
        content,
        null,
        null,
        null
    )
}
