package com.sangmee.fashionpeople.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FUser(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("instagramId")
    val instagramId: String?,
    @SerializedName("profileImage")
    val profileImage: String?,
    @SerializedName("followerNum")
    val followerNum: Int?,
    @SerializedName("followingNum")
    val followingNum: Int?,
    @SerializedName("evaluateNow")
    val evaluateNow: Boolean?
) : Parcelable
