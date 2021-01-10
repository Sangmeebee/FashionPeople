package com.sangmee.fashionpeople.ui.fragment.info.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.model.FeedImage
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.comment.CommentDialogFragment
import com.sangmee.fashionpeople.ui.fragment.grade.GradeDialogFragment
import com.sangmee.fashionpeople.ui.fragment.info.other.OtherFragment
import kotlinx.android.synthetic.main.fragment_info_detail.*
import kotlinx.android.synthetic.main.item_info_detail_feed.*

class InfoDetailFragment(
    private val customId: String,
    private val position: Int,
    private val mode: Int
) : Fragment(),
    DetailAdapter.OnClickListener {

    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DetailViewModel(customId) as T
            }
        }).get(DetailViewModel::class.java)
    }
    private val mainVm by activityViewModels<MainViewModel>()
    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    val userId = GlobalApplication.prefs.getString("${loginType}_custom_id", "empty")

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
        if (mode == 0) {
            viewModel.getImages(customId)
        } else {
            viewModel.getSaveImages(customId)
        }
        initViewPager()
        initObserve()
    }

    private fun initViewPager() {
        detailAdapter = DetailAdapter()
        detailAdapter.onClickListener = this@InfoDetailFragment
        vp_detail.apply {
            adapter = detailAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL
        }
    }

    private fun initObserve() {

        viewModel.feedImages.observe(viewLifecycleOwner, Observer {
            detailAdapter.setFeedImages(it)
            val saveImages = mutableListOf<String>()
            mainVm.saveImages.value?.let{
                for(feedImage in it){
                    saveImages.add(feedImage.imageName!!)
                }
                detailAdapter.setSaveItems(saveImages)
            }
        })

        viewModel.saveImages.observe(viewLifecycleOwner, Observer {
            detailAdapter.setFeedImages(it)
            val saveImages = mutableListOf<String>()
            mainVm.saveImages.value?.let{
                for(feedImage in it){
                    saveImages.add(feedImage.imageName!!)
                }
                detailAdapter.setSaveItems(saveImages)
            }
        })

        viewModel.isComplete.observe(viewLifecycleOwner, Observer {
                pb_loading.isVisible = false
                vp_detail.apply {
                    post {
                        setCurrentItem(position, false)
                        isVisible = true
                    }
                }
        })

        mainVm.saveImages.observe(viewLifecycleOwner, Observer {
            val saveImages = mutableListOf<String>()
            for(feedImage in it){
                saveImages.add(feedImage.imageName!!)
            }
            detailAdapter.setSaveItems(saveImages)
        })

        viewModel.saveComplete.observe(this, Observer {
            Toast.makeText(context, "사진을 저장했습니다.", Toast.LENGTH_SHORT).show()
            mainVm.getMySaveImage()
        })

        viewModel.deleteComplete.observe(this, Observer {
            Toast.makeText(context, "사진을 삭제했습니다.", Toast.LENGTH_SHORT).show()
            mainVm.getMySaveImage()
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

    override fun onClickSave(imageName: String) {
        viewModel.postSaveImage(imageName)
    }

    override fun onClickDelete(imageName: String) {
        viewModel.deleteSaveImage(imageName)
    }
}
