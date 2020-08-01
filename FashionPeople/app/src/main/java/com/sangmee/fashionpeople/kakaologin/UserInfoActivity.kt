package com.sangmee.fashionpeople.kakaologin

import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.sangmee.fashionpeople.MainActivity
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.retrofit.FUser
import com.sangmee.fashionpeople.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Callback
import retrofit2.Response

class UserInfoActivity : AppCompatActivity() {

    val pref = GlobalApplication.prefs



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)


        //툴바 세팅
        setSupportActionBar(app_toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        toolbar_title.setText("정보 입력")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)


        //버튼 클릭 (가입완료)
        authorization_btn.setOnClickListener {

            //회원 아이디
            val customId = pref.getString("custom_id", "empty")
            //닉네임
            val name = nickname.text.toString()
            val instagramId = instagram_id.text.toString()

            //회원정보 저장(retrofit2)
            RetrofitClient().getInstance().addUser(FUser(customId, name, instagramId)).enqueue(object: Callback<FUser> {
                override fun onFailure(call: retrofit2.Call<FUser>, t: Throwable) {
                    Log.d("sangmin_error", t.message)
                }

                override fun onResponse(call: retrofit2.Call<FUser>, response: Response<FUser>) {
                    Log.d("sangmin_success", response.body().toString())
                }
            })

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    //툴바 뒤로가기 버튼
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            android.R.id.home -> { //toolbar의 back키 눌렀을 때 동작
                pref.remove("custom_id")
                Log.d("sangmincheck", pref.getString("custom_id", "empty"))
                finish()
            }
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onBackPressed() {
        super.onBackPressed()
        pref.remove("custom_id")
    }


}