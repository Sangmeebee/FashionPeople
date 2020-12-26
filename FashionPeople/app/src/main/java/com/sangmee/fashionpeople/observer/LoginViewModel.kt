package com.sangmee.fashionpeople.observer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.subjects.BehaviorSubject

class LoginViewModel : ViewModel() {

    val customId = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val nextBtnEvent = SingleLiveEvent<Unit>()
    val backBtnEvent = SingleLiveEvent<Unit>()
    val authBtnEvent = SingleLiveEvent<Unit>()
    val emailBtnEvent = SingleLiveEvent<Unit>()
    val loadingSubject = BehaviorSubject.createDefault(false)

    fun clickNextBtn() {
        nextBtnEvent.value = Unit
    }

    fun clickBackBtn() {
        backBtnEvent.value = Unit
    }

    fun clickAuthBtn() {
        authBtnEvent.value = Unit
    }

    fun clickEmailBtn() {
        emailBtnEvent.value = Unit
    }
}
