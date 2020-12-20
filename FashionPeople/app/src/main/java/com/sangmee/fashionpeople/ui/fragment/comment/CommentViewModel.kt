package com.sangmee.fashionpeople.ui.fragment.comment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangmee.fashionpeople.data.model.Comment
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.repository.CommentRepository
import com.sangmee.fashionpeople.data.repository.FeedImageRepository
import com.sangmee.fashionpeople.util.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

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

    private val _submitEvent = SingleLiveEvent<Unit>()
    val submitEvent: LiveData<Unit>
        get() = _submitEvent

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

    fun updateFeedImageComment(userId: String, imageName: String, comment: Comment) {
        commentRepository.updateImageComment(userId, imageName, comment)
            .subscribeOn(Schedulers.io())
            .andThen(commentRepository.getImageComments(imageName))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _comments.value = it
                _submitEvent.call()
            }, {

            }).addTo(compositeDisposable)
    }


    fun clearDisposable() {
        compositeDisposable.clear()
    }

}
