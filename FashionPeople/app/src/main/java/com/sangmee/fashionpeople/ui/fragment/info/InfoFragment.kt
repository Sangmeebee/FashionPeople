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
import com.sangmee.fashionpeople.data.service.retrofit.RetrofitClient
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.ui.LoginActivity
import com.sangmee.fashionpeople.ui.SettingActivity
import com.sangmee.fashionpeople.ui.fragment.info.content.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InfoFragment : Fragment() {

    lateinit var customId: String

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

        //프로필 세팅
        runBlocking {
            val a = launch {
                RetrofitClient().getFUserService().getFUser(customId)
                    .enqueue(object : Callback<FUser> {
                        override fun onFailure(call: Call<FUser>, t: Throwable) {
                            Log.d("sangmin_error", t.message)
                        }

                        override fun onResponse(call: Call<FUser>, response: Response<FUser>) {
                            //닉네임 레트로핏으로 불러오기
                            val profileImgName = response.body()?.profileImage
                            val userName = response.body()?.name
                            val profileImg = view.findViewById<ImageView>(R.id.iv_profile)
                            val tvNickName = view.findViewById<TextView>(R.id.tv_nickname)

                            //유저 닉네임
                            userName?.let {
                                tvNickName.text = it
                            }
                            //프로필 이미지
                            profileImgName?.let {
                                Glide.with(context!!)
                                    .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${customId}/profile/${it}")
                                    .apply(RequestOptions().circleCrop())
                                    .error(R.drawable.user).into(profileImg)
                            }
                        }
                    })
            }
            a.join()
        }

        btn_setting.setOnClickListener {
            val intent = Intent(context, SettingActivity::class.java)
            startActivityForResult(intent, LOGOUT_CODE)
        }
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
