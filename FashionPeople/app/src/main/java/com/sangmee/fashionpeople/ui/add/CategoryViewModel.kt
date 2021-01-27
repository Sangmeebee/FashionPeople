package com.sangmee.fashionpeople.ui.add

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.dataSource.local.SearchLocalDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.BrandRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.Brand
import com.sangmee.fashionpeople.data.model.Style
import com.sangmee.fashionpeople.data.repository.BrandRepositoryImpl
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class CategoryViewModel : ViewModel() {

    private val brandRepository =
        BrandRepositoryImpl(BrandRemoteDataSourceImpl(), SearchLocalDataSourceImpl())
    private val compositeDisposable = CompositeDisposable()

    val brandList = MutableLiveData<List<Brand>>()
    val styleList = MutableLiveData<List<Style>>()

    val brandPopularList = MutableLiveData<List<Brand>>()
    val stylePopularList = MutableLiveData<List<Style>>()
    val isComplete = SingleLiveEvent<Any>()

    fun callBrand(brand: String) {
        brandRepository.getBrand(brand)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { isComplete.call() }
            .subscribe({ brandList.value = it }, { Log.e("Sangmeebee", it.message.toString()) })
            .addTo(compositeDisposable)
    }

    fun callBrandPopularList() {
        brandRepository.getPopularBrand()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { brandPopularList.value = it },
                { Log.e("Sangmeebee", it.message.toString()) })
            .addTo(compositeDisposable)
    }

    fun callStyle(style: String) {
        brandRepository.getStyle(style)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { isComplete.call() }
            .subscribe({ styleList.value = it }, { Log.e("Sangmeebee", it.message.toString()) })
            .addTo(compositeDisposable)
    }

    fun callStylePopularList() {
        brandRepository.getPopularStyle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { stylePopularList.value = it },
                { Log.e("Sangmeebee", it.message.toString()) })
            .addTo(compositeDisposable)
    }

    fun unbindViewModel() {
        compositeDisposable.clear()
    }
}
