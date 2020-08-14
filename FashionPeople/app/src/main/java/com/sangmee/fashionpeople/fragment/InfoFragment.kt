package com.sangmee.fashionpeople.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import com.sangmee.fashionpeople.retrofit.FUser
import com.sangmee.fashionpeople.retrofit.RetrofitClient
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Callback
import retrofit2.Response


class InfoFragment : Fragment() {

    lateinit var customId: String
    var customName: String? = null
    var profileImgName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customId = GlobalApplication.prefs.getString("custom_id", "???")
        //retrofit으로 회원 닉네임 알아내기


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        //툴바 세팅
        runBlocking {
            val a = launch {
                RetrofitClient().getInstance().getFUser(customId).enqueue(object: Callback<FUser> {
                    override fun onFailure(call: retrofit2.Call<FUser>, t: Throwable) {
                        Log.d("sangmin_error", t.message)
                    }

                    override fun onResponse(call: retrofit2.Call<FUser>, response: Response<FUser>) {
                        //닉네임 레트로핏으로 불러오기
                        customName = response.body()?.name.toString()
                        profileImgName = response.body()?.profileImage.toString()
                        //툴바 세팅
                        val toolbar = view.findViewById<Toolbar>(R.id.app_toolbar)
                        if(activity is AppCompatActivity){
                            (activity as AppCompatActivity).setSupportActionBar(toolbar)
                            val actionBar = (activity as AppCompatActivity).supportActionBar!!
                            val toolbar_title = view.findViewById<TextView>(R.id.toolbar_title)
                            toolbar_title.setText(customName)
                            actionBar.setDisplayShowTitleEnabled(false)
                            actionBar.setDisplayHomeAsUpEnabled(true)
                        }
                        val profileImg = view.findViewById<CircleImageView>(R.id.profile_image)

                        Glide.with(this@InfoFragment).load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/profile/1435930790/${profileImgName}").error(R.drawable.user).into(profileImg)

                    }
                })
            }
            a.join()
        }


        return view
    }

}
