package com.sangmee.fashionpeople.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Follower(
    @SerializedName("user_id")
    val user: FUser,
    @SerializedName("follower_person_id")
    val follower: FUser
) : Parcelable
