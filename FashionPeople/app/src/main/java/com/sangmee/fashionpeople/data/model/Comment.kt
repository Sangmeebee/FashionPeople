package com.sangmee.fashionpeople.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
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
) : Parcelable {
    constructor(content: String?) : this(
        null,
        content,
        null,
        null,
        null
    )
}
