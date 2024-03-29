package com.sangmee.fashionpeople.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Follower(
    @SerializedName("following")
    val isFollowing: Boolean?,
    @SerializedName("user")
    val user: FUser?,
    @SerializedName("followerId")
    val followerId: String?
) : Parcelable
