package com.sangmee.fashionpeople.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Following(
    @SerializedName("following_person_id")
    val followingPersonId: String?,
    @SerializedName("user_id")
    val userId: String?
) : Parcelable
