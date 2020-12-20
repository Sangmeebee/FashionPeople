package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.RankImage
import io.reactivex.rxjava3.core.Single

interface RankImageRemoteDataSource {

    fun getRankImages(): Single<Map<String, List<RankImage>>>
}
