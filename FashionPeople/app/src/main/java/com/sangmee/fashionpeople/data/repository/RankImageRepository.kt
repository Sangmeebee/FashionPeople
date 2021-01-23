package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.model.RankImage
import io.reactivex.rxjava3.core.Single

interface RankImageRepository {

    fun getManRankImages(): Single<Map<String, List<RankImage>>>
    fun getWomanRankImages(): Single<Map<String, List<RankImage>>>
}
