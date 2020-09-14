package com.sangmee.fashionpeople.retrofit.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment(
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("timeStamp")
    val timeStamp: String?,
    @SerializedName("userId")
    val userId: String?
): Parcelable