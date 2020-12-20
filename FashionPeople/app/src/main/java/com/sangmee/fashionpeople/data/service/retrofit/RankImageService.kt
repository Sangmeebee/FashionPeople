package com.sangmee.fashionpeople.data.service.retrofit

import com.sangmee.fashionpeople.data.model.RankImage
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface RankImageService {

    @GET("rankImage")
    fun getRankImages(): Single<Map<String, List<RankImage>>>
}