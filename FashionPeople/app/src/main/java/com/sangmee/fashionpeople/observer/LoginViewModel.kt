package com.sangmee.fashionpeople.observer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.repository.FUserRepository
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class LoginViewModel : ViewModel() {

    val customId = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val nextBtnEvent = SingleLiveEvent<Unit>()
    val backBtnEvent = SingleLiveEvent<Unit>()
    val emailBtnEvent = SingleLiveEvent<Unit>()
    val isOk = MutableLiveData<Boolean>(false)
    val loadingSubject = BehaviorSubject.createDefault(false)
    val isExist = MutableLiveData<Boolean>(false)
    private val compositeDisposable = CompositeDisposable()
    private val fUserRepository: FUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }

    fun checkIsEigenvalue(nickName: String) {
        fUserRepository.getIsEigenvalue(nickName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ isExist.value = it }, { Log.e("Sangmeebee", it.message.toString()) })
            .addTo(compositeDisposable)

    }


    fun clickNextBtn() {
        nextBtnEvent.value = Unit
    }

    fun clickBackBtn() {
        backBtnEvent.value = Unit
    }

    fun clickEmailBtn() {
        emailBtnEvent.value = Unit
    }

    fun unbindViewModel() {
        compositeDisposable.clear()
    }
}
