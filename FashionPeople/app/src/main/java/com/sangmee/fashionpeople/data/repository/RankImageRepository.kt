package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.model.RankImage
import io.reactivex.Single
import retrofit2.http.GET

interface RankImageRepository {

    fun getRankImages(): Single<Map<String, List<RankImage>>>
}