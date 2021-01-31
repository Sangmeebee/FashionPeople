package com.sangmee.fashionpeople.observer

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.SaveImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.model.stack.Stack
import com.sangmee.fashionpeople.data.model.stack.StackImplement
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import com.sangmee.fashionpeople.data.repository.SaveImageRepository
import com.sangmee.fashionpeople.data.repository.SaveImageRepositoryImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val saveImageRepository: SaveImageRepository by lazy {
        SaveImageRepositoryImpl(SaveImageRemoteDataSourceImpl())
    }
    private val userRepository = FUserRepositoryImpl(FUserRemoteDataSourceImpl())

    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    val userId = GlobalApplication.prefs.getString("${loginType}_custom_id", "empty")

    val user = MutableLiveData<FUser>()
    val saveImages = MutableLiveData<List<FeedImage>>()

    val tagName = MutableLiveData<String>()

    val homeFragments = Stack<Fragment>()
    val rankFragments = Stack<Fragment>()
    val searchFragments = Stack<Fragment>()
    val infoFragments = Stack<Fragment>()
    val tagList = Stack<String>()

    fun getUser() {
        userRepository.getFUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ user.value = it }, {})
    }

    fun getMySaveImage() {
        saveImageRepository.getSaveImages(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                saveImages.value = it
            }, {
            }).addTo(compositeDisposable)
    }

    fun unBindViewModel() {
        compositeDisposable.clear()
    }
}
