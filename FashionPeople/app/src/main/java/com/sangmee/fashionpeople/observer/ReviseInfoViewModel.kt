package com.sangmee.fashionpeople.observer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class ReviseInfoViewModel : ViewModel() {
    private val fUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }
    val compositeDisposable = CompositeDisposable()
    val loadingSubject = BehaviorSubject.createDefault(false)
    val finishSubject = BehaviorSubject.create<Unit>()
    val isExist = MutableLiveData<Boolean>(false)

    fun checkIsEigenvalue(nickName: String) {
        fUserRepository.getIsEigenvalue(nickName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ isExist.value = it }, { Log.e("Sangmeebee", it.message.toString()) })
            .addTo(compositeDisposable)

    }

    fun updateProfile(customId: String, fUser: FUser) {
        fUserRepository.updateUser(customId, fUser)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { loadingSubject.onNext(false) }
            .doOnTerminate { loadingSubject.onNext(true) }
            .doAfterTerminate { finishSubject.onNext(Unit) }
            .subscribe { }.addTo(compositeDisposable)
    }

    fun unbindViewModel() {
        compositeDisposable.clear()
    }
}
