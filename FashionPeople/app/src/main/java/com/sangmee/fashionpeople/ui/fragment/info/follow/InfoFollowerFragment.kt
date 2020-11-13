package com.sangmee.fashionpeople.ui.fragment.info.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.observer.FollowerViewModel
import kotlinx.android.synthetic.main.fragment_info_follow.*

class InfoFollowerFragment : Fragment() {

    private val vm by activityViewModels<FollowerViewModel>()
    private val followerAdapter = InfoFollowerAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        vm.callFollower()
        viewModelCallback()
    }

    private fun setRecyclerView() {
        rv_follow.apply {
            setHasFixedSize(true)
            adapter = followerAdapter
        }
    }

    private fun viewModelCallback() {

        vm.followers.observe(viewLifecycleOwner, Observer {
            vm.followers.value?.let { followerAdapter.clearAndAddItems(it) }
        })

    }
}
