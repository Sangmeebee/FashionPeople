package com.sangmee.fashionpeople.ui.fragment.rank

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.model.CustomDate
import com.sangmee.fashionpeople.data.repository.RankImageRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class RankViewModel(
    private val rankImageRepository: RankImageRepository
) : ViewModel() {


    private val compositeDisposable = CompositeDisposable()

    val dates = MutableLiveData<List<CustomDate>>()


    init {
        getRankImages()
    }

    private fun getRankImages() {
        Log.d("seunghwan", "rankviewmodel")
        rankImageRepository.getRankImages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
                Log.d("seunghwan", it.toString())
            }).addTo(compositeDisposable)
    }


    fun clearDisposable() {
        compositeDisposable.clear()
    }
}