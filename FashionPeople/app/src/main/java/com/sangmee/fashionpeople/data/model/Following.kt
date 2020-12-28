package com.sangmee.fashionpeople.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Following(
    @SerializedName("user")
    val user: FUser?,
    @SerializedName("followingId")
    val followingId: String?
) : Parcelable
