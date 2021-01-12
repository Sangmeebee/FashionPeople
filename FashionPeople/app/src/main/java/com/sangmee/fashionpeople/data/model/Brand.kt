package com.sangmee.fashionpeople.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Brand(
    @SerializedName("brandName")
    val brandName: String,
    @SerializedName("postNum")
    val postNum: Int
) : Parcelable
