package com.sangmee.fashionpeople.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.repository.FUserRepository
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class UserInfoVIewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val fUserRepository: FUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }

    val isExist = MutableLiveData<Boolean>(false)

    fun checkIsEigenvalue(nickName: String) {
        fUserRepository.getIsEigenvalue(nickName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ isExist.value = it }, { Log.e("Sangmeebee", it.message.toString()) })
            .addTo(compositeDisposable)

    }

    fun unbindViewModel() {
        compositeDisposable.clear()
    }
}
