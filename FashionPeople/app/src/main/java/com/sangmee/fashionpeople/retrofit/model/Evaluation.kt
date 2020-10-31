package com.sangmee.fashionpeople.retrofit.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Evaluation(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("evaluationPersonId")
    val evaluationPersonId: String?,
    @SerializedName("score")
    val score: Float?
) : Parcelable
