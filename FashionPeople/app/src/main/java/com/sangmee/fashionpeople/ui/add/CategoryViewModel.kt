package com.sangmee.fashionpeople.ui.add

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.dataSource.local.SearchLocalDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.BrandRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.Brand
import com.sangmee.fashionpeople.data.model.Style
import com.sangmee.fashionpeople.data.repository.BrandRepositoryImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class CategoryViewModel : ViewModel() {

    private val brandRepository = BrandRepositoryImpl(BrandRemoteDataSourceImpl(), SearchLocalDataSourceImpl())
    private val compositeDisposable = CompositeDisposable()

    val brandList = MutableLiveData<List<Brand>>()
    val styleList = MutableLiveData<List<Style>>()

    val loadingSubject = BehaviorSubject.createDefault(false)


    fun callBrand(brand: String) {
        brandRepository.getBrand(brand)
            .doOnSubscribe { loadingSubject.onNext(true) }
            .doAfterTerminate { loadingSubject.onNext(false) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ brandList.value = it }, { Log.e("Sangmeebee", it.message.toString()) })
            .addTo(compositeDisposable)
    }

    fun callStyle(style: String) {
        brandRepository.getStyle(style)
            .doOnSubscribe { loadingSubject.onNext(true) }
            .doAfterTerminate { loadingSubject.onNext(false) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ styleList.value = it }, { Log.e("Sangmeebee", it.message.toString()) })
            .addTo(compositeDisposable)
    }

    fun unbindViewModel() {
        compositeDisposable.clear()
    }
}
