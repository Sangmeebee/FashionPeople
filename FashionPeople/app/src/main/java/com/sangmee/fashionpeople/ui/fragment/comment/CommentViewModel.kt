package com.sangmee.fashionpeople.ui.fragment.comment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.data.model.FeedImage
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
    private val commentRepository: CommentRepository,
    private val feedImageRepository: FeedImageRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val imageNameSubject = BehaviorSubject.create<String>()

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>>
        get() = _comments

    private val _feedImage = MutableLiveData<FeedImage>()
    val feedImage: LiveData<FeedImage>
        get() = _feedImage

    init {
        imageNameSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getImageComments(it)
                getFeedImage(it)
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
                Log.d("seunghwan", it.toString())
            }).addTo(compositeDisposable)
    }

    private fun getFeedImage(imageName: String) {
        feedImageRepository.getFeedImageByName(imageName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _feedImage.value = it
            }, {

            }).addTo(compositeDisposable)
    }


    fun clearDisposable() {
        compositeDisposable.clear()
    }

}