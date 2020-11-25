package com.sangmee.fashionpeople.data.model


import com.google.gson.annotations.SerializedName

data class RankImage(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("rankTimeStamp")
    val rankTimeStamp: String?,
    @SerializedName("image")
    val feedImage: FeedImage?
)