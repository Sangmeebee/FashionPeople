package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.dataSource.remote.RankImageRemoteDataSource
import com.sangmee.fashionpeople.data.model.RankImage
import io.reactivex.rxjava3.core.Single

class RankImageRepositoryImpl(
    private val rankImageRemoteDataSource: RankImageRemoteDataSource
): RankImageRepository {

    override fun getRankImages(): Single<Map<String, List<RankImage>>> {
        return rankImageRemoteDataSource.getRankImages()
    }
}
