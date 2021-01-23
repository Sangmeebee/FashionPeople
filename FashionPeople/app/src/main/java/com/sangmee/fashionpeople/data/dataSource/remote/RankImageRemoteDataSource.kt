package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.RankImage
import io.reactivex.rxjava3.core.Single

interface RankImageRemoteDataSource {

    fun getManRankImages(): Single<Map<String, List<RankImage>>>
    fun getWomanRankImages(): Single<Map<String, List<RankImage>>>
}
