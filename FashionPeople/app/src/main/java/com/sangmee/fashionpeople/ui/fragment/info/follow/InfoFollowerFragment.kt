package com.sangmee.fashionpeople.ui.fragment.info.follow

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.observer.FollowViewModel
import com.sangmee.fashionpeople.observer.InfoViewModel
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.info.other.OtherFragment
import kotlinx.android.synthetic.main.fragment_info_follow.*
import java.util.*

class InfoFollowerFragment(private val userId: String) : Fragment() {

    private val infoVm: InfoViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private val vm: FollowViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private val mainVm by activityViewModels<MainViewModel>()

    private val followerAdapter by lazy {
        InfoFollowerAdapter({ setBtn(it) }, { vm.callOtherActivity(it) })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        if (userId == mainVm.userId) {
            mainVm.callFollowingsFollower()
        } else {
            vm.callFollowingsFollower(userId)
        }
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
            followerAdapter.clearAndAddItems(it)
        })
        vm.isFollowingsFollower.observe(viewLifecycleOwner, Observer {
            followerAdapter.clearAndAddButtonType(it)
        })

        mainVm.isFollowingsFollower.observe(viewLifecycleOwner, Observer {
            followerAdapter.clearAndAddButtonType(it)
        })

        vm.isFollowerComplete.observe(viewLifecycleOwner, Observer {
            crossfade()
        })

        vm.callActivity.observe(viewLifecycleOwner, Observer {
            (activity as MainActivity).replaceFragmentUseTagBackStack(
                OtherFragment.newInstance(it),
                mainVm.tagName.value!!
            )
        })
    }


    private fun setBtn(name: String) {
        if (userId == mainVm.userId) {
            mainVm.isFollowingsFollower.value?.let { isFollowings ->
                isFollowings[name]?.let { isFollowing ->
                    isFollowings[name] = !isFollowing
                    if (isFollowing) {
                        mainVm.deleteFollowing(name)
                        mainVm.followingNum.value?.let { mainVm.followingNum.value = it - 1 }
                    } else {
                        mainVm.updateFollowing(name)
                        mainVm.followingNum.value?.let { mainVm.followingNum.value = it + 1 }
                    }
                    mainVm.isFollowingsFollower.value = isFollowings
                }
            }

            mainVm.isFollowingsFollowing.value?.let { isFollowings ->
                isFollowings[name]?.let { isFollowing ->
                    isFollowings[name] = !isFollowing
                    mainVm.isFollowingsFollowing.value = isFollowings
                }
            }
        } else {
            vm.isFollowingsFollower.value?.let { isFollowings ->
                isFollowings[name]?.let { isFollowing ->
                    isFollowings[name] = !isFollowing
                    if (isFollowing) {
                        mainVm.deleteFollowing(name)
                        mainVm.isFollowingsFollower.value?.let { followings ->
                            followings[name] = false
                            mainVm.isFollowingsFollower.value = followings
                        }
                        infoVm.followingNum.value?.let { infoVm.followingNum.value = it - 1 }
                        mainVm.followingNum.value?.let { mainVm.followingNum.value = it - 1 }
                    } else {
                        mainVm.updateFollowing(name)
                        mainVm.isFollowingsFollower.value?.let { followings ->
                            followings[name] = true
                            mainVm.isFollowingsFollower.value = followings
                        }
                        infoVm.followingNum.value?.let { infoVm.followingNum.value = it + 1 }
                        mainVm.followingNum.value?.let { mainVm.followingNum.value = it + 1 }
                    }
                    vm.isFollowingsFollower.value = isFollowings
                }
            }

            vm.isFollowingsFollowing.value?.let { isFollowings ->
                isFollowings[name]?.let { isFollowing ->
                    isFollowings[name] = !isFollowing
                    vm.isFollowingsFollowing.value = isFollowings
                }
            }
            mainVm.isFollowingsFollowing.value?.let { isFs ->
                isFs[name]?.let { isF ->
                    isFs[name] = !isF
                    mainVm.isFollowingsFollowing.value = isFs
                }
            }
        }
    }

    private fun crossfade() {
        rv_follow?.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(500L)
                .setListener(null)
        }
    }
}
