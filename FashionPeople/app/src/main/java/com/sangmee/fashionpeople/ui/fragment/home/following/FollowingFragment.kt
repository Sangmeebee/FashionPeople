package com.sangmee.fashionpeople.ui.fragment.home.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.databinding.FragmentFollowingBinding
import com.sangmee.fashionpeople.ui.fragment.home.evaluate.EvaluateFeedAdapter

class FollowingFragment : Fragment(), EvaluateFeedAdapter.OnClickListener {

    private lateinit var binding: FragmentFollowingBinding
    private val pref = GlobalApplication.prefs
    lateinit var customId: String

    private lateinit var followingFeedAdapter: FollowingFeedAdapter



    private val viewModel: FollowingViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return FollowingViewModel() as T
            }
        }).get(FollowingViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_following, container, false)
        binding.lifecycleOwner = this@FollowingFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setId()
        initViewPager()
        initObserve()
    }

    private fun initObserve() {
        TODO("Not yet implemented")
    }

    private fun initViewPager() {
    }

    private fun setId() {
        customId = pref.getString("custom_id", "empty")
        viewModel.idSubject.onNext(customId)
    }


    override fun onDestroy() {
        viewModel.clearDisposable()
        super.onDestroy()
    }

    override fun onClickRatingBar(
        ratingBar: RatingBar?,
        rating: Float,
        fromUser: Boolean,
        feedImage: FeedImage
    ) {
        TODO("Not yet implemented")
    }

    override fun onClickComment(imageName: String) {
        TODO("Not yet implemented")
    }
}