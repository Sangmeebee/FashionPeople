package com.sangmee.fashionpeople.ui.fragment.info.follow

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.observer.FollowViewModel
import com.sangmee.fashionpeople.observer.InfoViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.info.other.OtherFragment
import kotlinx.android.synthetic.main.fragment_info_follow.*
import java.util.*

class InfoFollowerFragment(private val userId: String) : Fragment() {

    private val infoVm by activityViewModels<InfoViewModel>()
    private val vm by activityViewModels<FollowViewModel>()
    private val followerAdapter by lazy {
        InfoFollowerAdapter({
            vm.isFollowingsFollower.value?.let { isFollowings ->
                isFollowings[it]?.let { isFollowing ->
                    isFollowings[it] = !isFollowing
                    if (isFollowing) {
                        vm.deleteFollowing(it)
                        infoVm.followingNum.value?.let { infoVm.followingNum.value = it - 1 }
                    } else {
                        vm.updateFollowing(it)
                        infoVm.followingNum.value?.let { infoVm.followingNum.value = it + 1 }
                    }
                    vm.isFollowingsFollower.value = isFollowings
                }
            }

            vm.isFollowingsFollowing.value?.let { isFollowings ->
                isFollowings[it]?.let { isFollowing ->
                    isFollowings[it] = !isFollowing
                    vm.isFollowingsFollowing.value = isFollowings
                }
            }
        }, { customId, isFollower -> vm.callOtherActivity(customId, isFollower) })
    }

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
        vm.callFollower(userId)
        viewModelCallback()
        et_userName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val searchUserName = et_userName.text.toString().toLowerCase(Locale.getDefault())
                val correctUser = arrayListOf<FUser>()
                val isFollowingList = mutableMapOf<String, Boolean>()
                vm.followers.value?.let {
                    for (user in it) {
                        user.name?.let { name ->
                            if (name.toLowerCase(Locale.getDefault()).contains(searchUserName)) {
                                correctUser.add(user)
                            }
                        }
                    }
                    for (user in correctUser) {
                        vm.isFollowingsFollower.value?.let { t ->
                            isFollowingList.put(user.id!!, t[user.id]!!)
                        }
                    }
                }
                followerAdapter.clearAndAddItems(correctUser)
                followerAdapter.clearAndAddButtonType(isFollowingList)
            }
        })
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
        vm.isFollowingsFollower.observe(viewLifecycleOwner, Observer {
            vm.isFollowingsFollower.value?.let { followerAdapter.clearAndAddButtonType(it) }
        })

        vm.callActivity.observe(viewLifecycleOwner, Observer {
            if(vm.isFollower.value!!){
                (activity as MainActivity).replaceFragmentUseBackStack(OtherFragment.newInstance(it, 0))
            } else {
                (activity as MainActivity).replaceFragmentUseBackStack(OtherFragment.newInstance(it, 1))
            }
        })
    }
}
