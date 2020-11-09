package com.sangmee.fashionpeople.retrofit.model

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
    @SerializedName("images")
    val images: List<FeedImage>?
): Parcelable
