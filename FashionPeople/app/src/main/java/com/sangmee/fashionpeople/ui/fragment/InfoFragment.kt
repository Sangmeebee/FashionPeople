package com.sangmee.fashionpeople.ui.fragment

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
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import com.sangmee.fashionpeople.retrofit.RetrofitClient
import com.sangmee.fashionpeople.retrofit.model.FUser
import com.sangmee.fashionpeople.retrofit.model.FeedImage
import com.sangmee.fashionpeople.ui.FeedImageAdapter
import com.sangmee.fashionpeople.ui.LoginActivity
import com.sangmee.fashionpeople.ui.SettingActivity
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InfoFragment : Fragment() {

    lateinit var customId: String
    private val feedImageAdapter by lazy {
        FeedImageAdapter(customId)
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

        val battleImage = GlobalApplication.prefs.getString("battleImage", "")
        if (battleImage != "") {
            Glide.with(context!!)
                .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${customId}/feed/${battleImage}")
                .error(R.drawable.plus).into(iv_plus)
            iv_plus.scaleType = ImageView.ScaleType.CENTER_CROP
        }



        runBlocking {
            val a = launch {
                RetrofitClient().getFUserService().getFUser(customId)
                    .enqueue(object : Callback<FUser> {
                        override fun onFailure(call: Call<FUser>, t: Throwable) {
                            Log.d("sangmin_error", t.message)
                        }

                        override fun onResponse(call: Call<FUser>, response: Response<FUser>) {
                            //닉네임 레트로핏으로 불러오기
                            val profileImgName = response.body()?.profileImage.toString()
                            val introduce = response.body()?.instagramId
                            val userId = response.body()?.name
                            val profileImg = view.findViewById<ImageView>(R.id.iv_info_user)
                            val introduceTv =
                                view.findViewById<TextView>(R.id.tv_info_user_introduce)
                            val userName = view.findViewById<TextView>(R.id.tv_info_user_name)

                            introduce?.let {
                                introduceTv.text = it
                            }

                            userId?.let {
                                userName.text = it
                            }

                            Glide.with(context!!)
                                .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${customId}/profile/${profileImgName}")
                                .apply(RequestOptions().circleCrop())
                                .error(R.drawable.user).into(profileImg)

                        }
                    })
            }
            a.join()
        }


        rv_user_image.apply {
            adapter = feedImageAdapter
        }

        btn_setting.setOnClickListener {
            val intent = Intent(context, SettingActivity::class.java)
            startActivityForResult(intent, LOGOUT_CODE)
        }

        getFeedImages()


    }

    private fun getFeedImages() {
        if (customId != "") {
            RetrofitClient().getFeedImageService().getFeedImages(customId)
                .enqueue(object : Callback<List<FeedImage>> {
                    override fun onResponse(
                        call: Call<List<FeedImage>>,
                        response: Response<List<FeedImage>>
                    ) {
                        response.body()?.let { feedImages ->
                            feedImageAdapter.setFeedImages(feedImages)
                            Log.d("sangmin", feedImages.toString())
                        }
                    }

                    override fun onFailure(call: Call<List<FeedImage>>, t: Throwable) {
                        Log.d("fail", t.message)
                    }
                })
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
