package com.sangmee.fashionpeople.ui.fragment.search.account

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.local.SearchLocalDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.BrandRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.repository.BrandRepositoryImpl
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchAccountViewModel : ViewModel() {

    private val brandRepository =
        BrandRepositoryImpl(BrandRemoteDataSourceImpl(), SearchLocalDataSourceImpl())
    private val fUserRepository = FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    private val compositeDisposable = CompositeDisposable()


    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    val customId = GlobalApplication.prefs.getString("${loginType}_custom_id", "empty")

    val recentList = MutableLiveData<List<FUser>>()
    val userList = MutableLiveData<List<FUser>>()
    val isComplete = SingleLiveEvent<Any>()
    val isEmpty = SingleLiveEvent<Any>()

    fun callSearchUser(nickName: String) {
        fUserRepository.getSearchUser(nickName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { isComplete.call() }
            .subscribe({ userList.value = it }, { Log.e("Sangmeebee", it.message.toString()) })
            .addTo(compositeDisposable)
    }

    fun callRecentList() {
        recentList.value = brandRepository.readRecentSearchUser("${customId}_accountList")
    }

    fun postRecentList(user: FUser) {
        brandRepository.saveRecentSearchUser("${customId}_accountList", user)
    }

    fun deleteRecentList(user: FUser) {
        brandRepository.deleteRecentSearchUser("${customId}_accountList", user)
    }

    fun clearRecentList() {
        brandRepository.clearRecentSearchUser("${customId}_accountList")
    }

    fun unbindViewModel() {
        compositeDisposable.clear()
    }
}
