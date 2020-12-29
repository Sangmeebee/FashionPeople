package com.sangmee.fashionpeople.observer

import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
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
