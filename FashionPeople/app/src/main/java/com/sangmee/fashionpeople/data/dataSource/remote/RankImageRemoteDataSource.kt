package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.RankImage
import io.reactivex.Single
import retrofit2.http.GET

interface RankImageRemoteDataSource {

    fun getRankImages(): Single<Map<String, List<RankImage>>>
}