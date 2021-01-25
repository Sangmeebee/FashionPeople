package com.sangmee.fashionpeople.ui.fragment.search.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.comment.CommentDialogFragment
import com.sangmee.fashionpeople.ui.fragment.grade.GradeDialogFragment
import com.sangmee.fashionpeople.ui.fragment.info.other.OtherFragment
import com.sangmee.fashionpeople.ui.fragment.tag.TagDialogFragment
import com.willy.ratingbar.BaseRatingBar
import kotlinx.android.synthetic.main.fragment_info_detail.*

class SearchDetailFragment(private val feedImages: List<FeedImage>, private val position: Int) :
    Fragment(), SearchDetailAdapter.OnClickListener {

    private val viewModel: SearchDetailViewModel by viewModels()
    private val mainVm by activityViewModels<MainViewModel>()
    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    val userId = GlobalApplication.prefs.getString("${loginType}_custom_id", "empty")
    private var pos: Int? = null

    private lateinit var detailAdapter: SearchDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.isAdded.value = false
        detailAdapter = SearchDetailAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initObserve()
    }

    private fun initViewPager() {
        detailAdapter.onClickListener = this@SearchDetailFragment
        Log.d("Sangmeebee", viewModel.currentIndex.value.toString())
        vp_detail.apply {
            adapter = detailAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL
            post {
                viewModel.isAdded.value?.let {
                    if (!it) {
                        setCurrentItem(position, false)
                        viewModel.isAdded.value = true
                    } else{
                        setCurrentItem(viewModel.currentIndex.value!!, false)
                    }

                }
                crossfade()
            }
        }

        vp_detail.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.currentIndex.value = position
            }
        })
    }


    private fun initObserve() {

        detailAdapter.setFeedImages(feedImages)
        val saveImages = mutableListOf<String>()
        mainVm.saveImages.value?.let {
            for (feedImage in it) {
                saveImages.add(feedImage.imageName!!)
            }
            detailAdapter.setSaveItems(saveImages, null)
        }


        mainVm.saveImages.observe(viewLifecycleOwner, Observer {
            val saveImages = mutableListOf<String>()
            for (feedImage in it) {
                saveImages.add(feedImage.imageName!!)
            }

            detailAdapter.setSaveItems(saveImages, pos)
        })

        viewModel.saveComplete.observe(this, Observer {
            Toast.makeText(context, "사진을 저장했습니다.", Toast.LENGTH_SHORT).show()
            mainVm.getMySaveImage()
        })

        viewModel.deleteComplete.observe(this, Observer {
            Toast.makeText(context, "사진을 삭제했습니다.", Toast.LENGTH_SHORT).show()
            mainVm.getMySaveImage()
        })

        viewModel.updateFeedImage.observe(this, Observer {
            detailAdapter.updateItem(it)
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

    private fun showTagFragment(feedImage: FeedImage) {
        TagDialogFragment.newInstance(feedImage)
            .show(childFragmentManager, TagDialogFragment.TAG)
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

    override fun onClickSave(imageName: String, position: Int) {
        viewModel.postSaveImage(imageName)
        pos = position
    }

    override fun onClickDelete(imageName: String, position: Int) {
        viewModel.deleteSaveImage(imageName)
        pos = position
    }

    override fun onClickTag(feedImage: FeedImage) {
        showTagFragment(feedImage)
    }

    override fun onClickRatingBar(
        ratingBar: BaseRatingBar?,
        rating: Float,
        fromUser: Boolean,
        feedImage: FeedImage
    ) {
        feedImage.imageName?.let { viewModel.ratingClick(it, rating) }
    }

    private fun crossfade() {
        vp_detail.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(500L)
                .setListener(null)
        }
    }
}
