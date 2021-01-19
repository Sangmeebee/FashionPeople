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
    @SerializedName("introduce")
    val introduce: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("weight")
    val weight: Int?,
    @SerializedName("profileImage")
    var profileImage: String?,
    @SerializedName("evaluateNow")
    val evaluateNow: Boolean?,
    @SerializedName("followers")
    val followers: List<Follower>?,
    @SerializedName("followings")
    val followings: List<Following>?
) : Parcelable
