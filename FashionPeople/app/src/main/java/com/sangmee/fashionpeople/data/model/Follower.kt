package com.sangmee.fashionpeople.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Follower(
    @SerializedName("follower_person_id")
    val followerPersonId: String?,
    @SerializedName("user_id")
    val userId: String?
) : Parcelable
