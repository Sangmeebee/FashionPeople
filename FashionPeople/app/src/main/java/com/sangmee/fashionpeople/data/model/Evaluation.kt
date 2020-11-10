package com.sangmee.fashionpeople.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Evaluation(
    @SerializedName("evaluationPersonId")
    val evaluationPersonId: String?,
    @SerializedName("score")
    val score: Float?
) : Parcelable
