package com.sangmee.fashionpeople.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import com.sangmee.fashionpeople.retrofit.RetrofitClient
import com.sangmee.fashionpeople.retrofit.model.FUser
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private var callback: SessionCallback = SessionCallback()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        iv_kakao_login.setOnClickListener {
            Toast.makeText(this, "카카오톡으로 로그인합니다.", Toast.LENGTH_SHORT).show()
            //카카오 콜백 추가
            Session.getCurrentSession().addCallback(callback)
            Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this)
        }

    }

    //카카오톡 로그인 콜백
    inner class SessionCallback : ISessionCallback {
        lateinit var custom_id: String
        override fun onSessionOpenFailed(exception: KakaoException?) {
            Log.e("sangmin", "Session Call back :: onSessionOpenFailed: ${exception?.message}")
        }

        override fun onSessionOpened() {
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {

                override fun onFailure(errorResult: ErrorResult?) {
                    Log.i("sangmin", "Session Call back :: on failed ${errorResult?.errorMessage}")
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    Log.i(
                        "sangmin",
                        "Session Call back :: onSessionClosed ${errorResult?.errorMessage}"
                    )
                }

                override fun onSuccess(result: MeV2Response?) {
                    Log.d("sangmin", "연결 성공")
                    custom_id = result!!.id.toString()
                    GlobalApplication.prefs.setString("custom_id", custom_id)
                    Log.i("sangmin", "아이디 : ${custom_id}")
                    Log.i("Log", "이메일 : ${result.kakaoAccount.email}")
                    Log.i("Log", "성별 : ${result.kakaoAccount.gender}")
                    Log.i("Log", "생일 : ${result.kakaoAccount.birthday}")
                    Log.i("Log", "연령대 : ${result.kakaoAccount.ageRange}")

                    checkNotNull(result) { "session response null" }
                    // 데이터베이스에 아이디 이미 있는지 체크
                    RetrofitClient().getFUserService().getAllFUser().enqueue(object :
                        Callback<List<FUser>> {
                        override fun onFailure(call: retrofit2.Call<List<FUser>>, t: Throwable) {
                            Log.d("fashionPeople_error", t.message)
                        }

                        override fun onResponse(
                            call: retrofit2.Call<List<FUser>>,
                            response: Response<List<FUser>>
                        ) {
                            Log.d(
                                "fashionPeople_success",
                                response.body()!![0].id.toString() + response.body()!!.size
                            )
                            val res = response.body()!!
                            var exist = false
                            for (fUser in res) {
                                if (fUser.id == custom_id) {
                                    exist = true
                                }
                            }
                            redirectUserInfoActivity(exist)
                        }
                    })
                }
            })
        }

    }

    //화면전환 메소드
    private fun redirectUserInfoActivity(exist: Boolean) {
        val intent =
            if (exist) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, UserInfoActivity::class.java)
            }
        startActivity(intent)
        if (exist) {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //카카오톡 로그인 결과
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.i("Log", "session get current session")
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //카카오 로그인
    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)

    }
}
