package com.sangmee.fashionpeople.ui.fragment.home.evaluate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentEvaluateBinding
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import com.sangmee.fashionpeople.retrofit.model.FeedImage
import com.sangmee.fashionpeople.ui.fragment.home.HomeFeedAdapter

class EvaluateFragment : Fragment(), HomeFeedAdapter.OnClickListener {

    private lateinit var binding: FragmentEvaluateBinding
    val pref = GlobalApplication.prefs
    lateinit var customId: String

    private val viewModel: EvaluateViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return EvaluateViewModel() as T
            }
        }).get(EvaluateViewModel::class.java)
    }

    private lateinit var homeFeedAdapter: HomeFeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_evaluate, container, false)
        binding.lifecycleOwner = this@EvaluateFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setId()
        initViewPager()
        initObserve()
    }

    private fun setId() {
        customId = pref.getString("custom_id", "empty")

        viewModel.idSubject.onNext(customId)
    }

    private fun initViewPager() {
        homeFeedAdapter = HomeFeedAdapter(myId = customId)
        homeFeedAdapter.onClickListener = this@EvaluateFragment
        binding.vpEvaluate.apply {
            adapter = homeFeedAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.nowPageSubject.onNext(position)
                    Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun initObserve() {
        viewModel.feedImages.observe(this@EvaluateFragment, Observer {
            it?.let {
                homeFeedAdapter.setFeedImages(it)
            }
        })

        viewModel.nowPage.observe(this@EvaluateFragment, Observer {
            it?.let {
                if (homeFeedAdapter.itemCount - 1 > binding.vpEvaluate.currentItem) {
                    binding.vpEvaluate.currentItem = it
                }
            }
        })

        viewModel.updateFeedImages.observe(this@EvaluateFragment, Observer {
            it?.let {
                homeFeedAdapter.updateItem(it)
            }
        })

        viewModel.evaluateMessage.observe(this@EvaluateFragment, Observer {
            Log.d("seunghwan", "평가")
            Toast.makeText(context, "평가가 완료되었습니다", Toast.LENGTH_SHORT).show()
        })
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
        ratingBar?.rating = rating
        feedImage.imageName?.let { viewModel.ratingClick(it, rating) }
    }
}