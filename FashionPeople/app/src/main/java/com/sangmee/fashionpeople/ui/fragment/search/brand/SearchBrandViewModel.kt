package com.sangmee.fashionpeople.ui.fragment.search.brand

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.dataSource.local.SearchLocalDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.BrandRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.Brand
import com.sangmee.fashionpeople.data.repository.BrandRepositoryImpl
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchBrandViewModel : ViewModel() {

    private val brandRepository =
        BrandRepositoryImpl(BrandRemoteDataSourceImpl(), SearchLocalDataSourceImpl())
    private val compositeDisposable = CompositeDisposable()

    val brandList = MutableLiveData<List<Brand>>()
    val recentList = MutableLiveData<List<String>>()
    val isComplete = SingleLiveEvent<Any>()
    val isEmpty = SingleLiveEvent<Any>()

    fun callBrand(brand: String) {
        brandRepository.getBrand(brand)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { isComplete.call() }
            .subscribe({ brandList.value = it }, { Log.e("Sangmeebee", it.message.toString()) })
            .addTo(compositeDisposable)
    }

    fun callRecentList() {
        recentList.value = brandRepository.readRecentSearchQuery("brandList")
    }

    fun postRecentList(query: String) {
        brandRepository.saveRecentSearchQuery("brandList", query)
    }

    fun deleteRecentList(query: String) {
        brandRepository.deleteRecentSearchQuery("brandList", query)
    }

    fun clearRecentList() {
        brandRepository.clearRecentSearchQuery("brandList")
    }

    fun unbindViewModel() {
        compositeDisposable.clear()
    }
}
