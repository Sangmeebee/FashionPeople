package com.sangmee.fashionpeople.data.model


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("content")
    val content: String?,
    @SerializedName("commentPersonId")
    val commentPersonId: String?,
    @SerializedName("current_date_time")
    val currentDateTime: String?,
    @SerializedName("user")
    val user: FUser?
) {
    constructor(content: String?, commentPersonId: String?) : this(
        content,
        commentPersonId,
        null,
        null
    )
}
