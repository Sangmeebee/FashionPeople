package com.sangmee.fashionpeople.retrofit.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedImage(
    @SerializedName("imageName")
    val imageName: String?,
    @SerializedName("timeStamp")
    val timeStamp: String?,
    @SerializedName("style")
    val style: String?,
    @SerializedName("top")
    val top: String?,
    @SerializedName("pants")
    val pants: String?,
    @SerializedName("shoes")
    val shoes: String?,
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("battleNow")
    val battleNow: Boolean?
) : Parcelable
