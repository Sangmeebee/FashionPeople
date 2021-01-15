package com.sangmee.fashionpeople.ui.fragment.search.style

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.dataSource.local.SearchLocalDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.BrandRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.Style
import com.sangmee.fashionpeople.data.repository.BrandRepositoryImpl
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class SearchStyleViewModel : ViewModel() {

    private val brandRepository = BrandRepositoryImpl(BrandRemoteDataSourceImpl(), SearchLocalDataSourceImpl())
    private val compositeDisposable = CompositeDisposable()

    val styleList = MutableLiveData<List<Style>>()
    val recentList = MutableLiveData<List<String>>()
    val isComplete = SingleLiveEvent<Any>()
    val isEmpty = SingleLiveEvent<Any>()

    fun callStyle(style: String) {
        brandRepository.getStyle(style)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { isComplete.call() }
            .subscribe({ styleList.value = it }, { Log.e("Sangmeebee", it.message.toString()) })
            .addTo(compositeDisposable)
    }


    fun callRecentList() {
        recentList.value = brandRepository.readRecentSearchQuery("styleList")
    }

    fun postRecentList(query: String) {
        brandRepository.saveRecentSearchQuery("styleList", query)
    }

    fun deleteRecentList(query: String) {
        brandRepository.deleteRecentSearchQuery("styleList", query)
    }

    fun clearRecentList(){
        brandRepository.clearRecentSearchQuery("styleList")
    }

    fun unbindViewModel() {
        compositeDisposable.clear()
    }
}
