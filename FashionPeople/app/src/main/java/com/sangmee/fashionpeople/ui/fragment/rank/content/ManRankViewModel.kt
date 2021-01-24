package com.sangmee.fashionpeople.ui.fragment.rank.content

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.dataSource.remote.RankImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.CustomDate
import com.sangmee.fashionpeople.data.repository.RankImageRepositoryImpl
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class ManRankViewModel : ViewModel() {

    private val rankImageRepository = RankImageRepositoryImpl(RankImageRemoteDataSourceImpl())

    private val compositeDisposable = CompositeDisposable()

    val dates = MutableLiveData<List<CustomDate>>()
    val isComplete = SingleLiveEvent<Any>()

    fun getRankImages() {
        rankImageRepository.getManRankImages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { isComplete.call() }
            .subscribe({ map ->
                val arrayList = ArrayList<CustomDate>()
                for (key in map.keys) {
                    map[key]?.let { CustomDate(key, it) }?.let { arrayList.add(it) }
                }
                arrayList.sortWith(Comparator { o1, o2 ->
                    o2.date.compareTo(o1.date)
                })
                dates.value = arrayList.toList()
            }, {
                Log.e("Sangmeebee", it.message.toString())
            }).addTo(compositeDisposable)
    }


    fun clearDisposable() {
        compositeDisposable.clear()
    }
}
