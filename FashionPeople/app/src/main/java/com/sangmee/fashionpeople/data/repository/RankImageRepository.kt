package com.sangmee.fashionpeople.data.repository

import com.sangmee.fashionpeople.data.model.RankImage
import io.reactivex.rxjava3.core.Single

interface RankImageRepository {

    fun getRankImages(): Single<Map<String, List<RankImage>>>
}
