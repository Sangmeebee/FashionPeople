package com.sangmee.fashionpeople.data.model


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("comment")
    val content: String?,
    @SerializedName("commentPersonId")
    val commentPersonId: String?,
    @SerializedName("currentDateTime")
    val currentDateTime: String?,
    @SerializedName("imageId")
    val imageId: String?,
    @SerializedName("user_id")
    val user: FUser?
) {
    constructor(content: String?, commentPersonId: String?, imageId: String?) : this(
        content,
        commentPersonId,
        null,
        imageId
    )
}