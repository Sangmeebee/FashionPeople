package com.sangmee.fashionpeople.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedImage(
    @SerializedName("imageName")
    val imageName: String?,
    @SerializedName("timeStamp")
    val timeStamp: String?,
    @SerializedName("style")
    val style: String?,
    @SerializedName("top")
    val top: String?,
    @SerializedName("pants")
    val pants: String?,
    @SerializedName("shoes")
    val shoes: String?,
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("evaluateNow")
    val evaluateNow: Boolean,
    @SerializedName("evaluations")
    val evaluations: List<Evaluation>?,
    @SerializedName("comments")
    val comments: List<Comment>?,
    @SerializedName("resultRating")
    val resultRating: Float?,
    @SerializedName("resultTimeStamp")
    val resultTimeStamp: String?,
    @SerializedName("user")
    val user: FUser?
) : Parcelable {
    constructor(
        imageName: String?,
        style: String?,
        top: String?,
        pants: String?,
        shoes: String?,
        evaluateNow: Boolean,
        resultRating: Float?
    ) : this(
        imageName,
        null,
        style,
        top,
        pants,
        shoes,
        null,
        evaluateNow,
        null,
        null,
        resultRating,
        null,
        null
    )
}
