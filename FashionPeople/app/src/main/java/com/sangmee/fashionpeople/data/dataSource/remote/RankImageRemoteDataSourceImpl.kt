package com.sangmee.fashionpeople.data.dataSource.remote

import com.sangmee.fashionpeople.data.model.RankImage
import com.sangmee.fashionpeople.data.service.retrofit.RetrofitClient
import io.reactivex.rxjava3.core.Single

class RankImageRemoteDataSourceImpl : RankImageRemoteDataSource {

    override fun getManRankImages(): Single<Map<String, List<RankImage>>> {
        return RetrofitClient.getRankImageService().getManRankImages()
    }

    override fun getWomanRankImages(): Single<Map<String, List<RankImage>>> {
        return RetrofitClient.getRankImageService().getWomanRankImages()
    }

}
