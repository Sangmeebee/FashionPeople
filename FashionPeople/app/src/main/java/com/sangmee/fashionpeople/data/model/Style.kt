package com.sangmee.fashionpeople.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Style(
    @SerializedName("styleName")
    val styleName: String,
    @SerializedName("postNum")
    val postNum: Int
) : Parcelable
