package com.sangmee.fashionpeople.retrofit.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedImage(
    @SerializedName("comments")
    val comments: List<Comment>?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("imageName")
    val imageName: String?,
    @SerializedName("likes")
    val likes: List<Like>?,
    @SerializedName("timeStamp")
    val timeStamp: String?,
    @SerializedName("userId")
    val userId: String?
): Parcelable