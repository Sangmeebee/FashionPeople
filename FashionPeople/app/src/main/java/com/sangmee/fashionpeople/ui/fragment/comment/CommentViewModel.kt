package com.sangmee.fashionpeople.ui.fragment.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.data.repository.CommentRepository
import com.sangmee.fashionpeople.data.repository.FeedImageRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import retrofit2.http.Path

class CommentViewModel(
    private val commentRepository: CommentRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val imageNameSubject = BehaviorSubject.create<String>()

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>>
        get() = _comments

    init {
        imageNameSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getImageComments(it)
            }, {

            }).addTo(compositeDisposable)
    }


    private fun getImageComments(imageName: String) {
        commentRepository.getImageComments(imageName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _comments.value = it
            }, {

            }).addTo(compositeDisposable)
    }


    fun clearDisposable() {
        compositeDisposable.clear()
    }

}