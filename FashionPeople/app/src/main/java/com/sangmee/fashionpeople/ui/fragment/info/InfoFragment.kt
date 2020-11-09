package com.sangmee.fashionpeople.ui.fragment.info

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.local.LocalDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.RemoteDataSourceImpl
import com.sangmee.fashionpeople.data.repository.Repository
import com.sangmee.fashionpeople.data.repository.RepositoryImpl
import com.sangmee.fashionpeople.ui.LoginActivity
import com.sangmee.fashionpeople.ui.SettingActivity
import com.sangmee.fashionpeople.ui.fragment.info.content.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_info.*


class InfoFragment : Fragment() {

    lateinit var customId: String
    private val repository: Repository by lazy {
        RepositoryImpl(
            LocalDataSourceImpl(), RemoteDataSourceImpl()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customId = GlobalApplication.prefs.getString("custom_id", "")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //tablayout 세팅
        viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(tl_container, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "게시물"
                else -> tab.text = "저장함"
            }
        }.attach()

        setProfile(view)

        btn_setting.setOnClickListener {
            val intent = Intent(context, SettingActivity::class.java)
            startActivityForResult(intent, LOGOUT_CODE)
        }
    }

    private fun setProfile(view: View) {
        //프로필 세팅
        repository.getFUser(customId, success = {
            val profileImgName = it.profileImage
            val userName = it.name
            val profileImg = view.findViewById<ImageView>(R.id.iv_profile)
            val tvNickName = view.findViewById<TextView>(R.id.tv_nickname)

            userName?.let { name ->
                tvNickName.text = name
            }
            //프로필 이미지
            profileImgName?.let {
                Glide.with(context!!)
                    .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${customId}/profile/${it}")
                    .apply(RequestOptions().circleCrop())
                    .error(R.drawable.user).into(profileImg)
            }
        }, failed = { Log.e("fashionPeopleError", it) })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGOUT_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            activity?.finish()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        private const val LOGOUT_CODE = 210
    }


}
