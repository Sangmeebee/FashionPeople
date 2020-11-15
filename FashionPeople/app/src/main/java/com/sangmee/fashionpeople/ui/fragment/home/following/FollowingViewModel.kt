package com.sangmee.fashionpeople.ui.fragment.home.following

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class FollowingViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _myId = MutableLiveData<String>()
    val myId: LiveData<String>
        get() = _myId

    val idSubject = BehaviorSubject.create<String>()


    fun clearDisposable() {
        compositeDisposable.clear()
    }
}