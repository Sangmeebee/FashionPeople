package com.sangmee.fashionpeople.ui.fragment.info.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.dataSource.remote.FeedImageRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.data.repository.FeedImageRepositoryImpl
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.comment.CommentDialogFragment
import com.sangmee.fashionpeople.ui.fragment.grade.GradeDialogFragment
import com.sangmee.fashionpeople.ui.fragment.info.other.OtherFragment
import kotlinx.android.synthetic.main.fragment_info_detail.*

class InfoDetailFragment(private val customId: String, private val position: Int) : Fragment(),
    DetailAdapter.OnClickListener {

    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DetailViewModel(
                    FeedImageRepositoryImpl(feedImageRemoteDataSource = FeedImageRemoteDataSourceImpl())
                ) as T
            }
        }).get(DetailViewModel::class.java)
    }

    private lateinit var detailAdapter: DetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initObserve()
    }

    private fun initViewPager() {
        detailAdapter = DetailAdapter(myId = customId)
        detailAdapter.onClickListener = this@InfoDetailFragment
        vp_detail.apply {
            adapter = detailAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL
        }
    }

    private fun initObserve() {
        viewModel.idSubject.onNext(customId)

        viewModel.feedImages.observe(viewLifecycleOwner, Observer {
            detailAdapter.setFeedImages(it)
        })

        viewModel.isComplete.observe(viewLifecycleOwner, Observer {
            if (it) {
                pb_loading.isVisible = !it
                vp_detail.apply {
                    post {
                        setCurrentItem(position, false)
                        isVisible = it
                    }
                }
            }
        })
    }

    private fun showCommentFragment(imageName: String) {
        CommentDialogFragment.newInstance(imageName)
            .show(childFragmentManager, CommentDialogFragment.TAG)
    }

    private fun showGradeFragment(feedImage: FeedImage) {
        GradeDialogFragment.newInstance(feedImage)
            .show(childFragmentManager, GradeDialogFragment.TAG)
    }

    override fun onDestroy() {
        viewModel.clearDisposable()
        super.onDestroy()
    }

    override fun onClickComment(imageName: String) {
        showCommentFragment(imageName)
    }

    override fun onClickGrade(feedImage: FeedImage) {
        showGradeFragment(feedImage)
    }

    override fun onClickProfile(feedImage: FeedImage) {
        feedImage.user?.id?.let { OtherFragment.newInstance(it) }?.let {
            (activity as MainActivity).replaceFragmentUseBackStack(it)
        }
    }
}
