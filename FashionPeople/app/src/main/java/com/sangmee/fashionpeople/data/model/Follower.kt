package com.sangmee.fashionpeople.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Follower(
    @SerializedName("user")
    val user: FUser?,
    @SerializedName("follower")
    val follower: FUser?
) : Parcelable
