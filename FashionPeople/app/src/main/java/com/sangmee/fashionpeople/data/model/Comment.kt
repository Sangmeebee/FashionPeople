package com.sangmee.fashionpeople.data.model


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("content")
    val content: String?,
    @SerializedName("commentPersonId")
    val commentPersonId: String?,
    @SerializedName("current_date_time")
    val currentDateTime: String?,
    @SerializedName("image_name")
    val imageId: String?,
    @SerializedName("user")
    val user: FUser?
) {
    constructor(content: String?, commentPersonId: String?, imageId: String?) : this(
        content,
        commentPersonId,
        null,
        imageId,
        null
    )
}