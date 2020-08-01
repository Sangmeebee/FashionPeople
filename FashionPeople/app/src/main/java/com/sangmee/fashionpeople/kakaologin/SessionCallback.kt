package com.sangmee.fashionpeople.kakaologin

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.kakao.auth.ISessionCallback
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException

class SessionCallback : ISessionCallback {
    lateinit var custom_id : String
    override fun onSessionOpenFailed(exception: KakaoException?) {
        Log.e("Log", "Session Call back :: onSessionOpenFailed: ${exception?.message}")
    }

    override fun onSessionOpened() {
        UserManagement.getInstance().me(object : MeV2ResponseCallback() {

            override fun onFailure(errorResult: ErrorResult?) {
                Log.i("Log", "Session Call back :: on failed ${errorResult?.errorMessage}")
            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
                Log.i("Log", "Session Call back :: onSessionClosed ${errorResult?.errorMessage}")

            }

            override fun onSuccess(result: MeV2Response?) {
                custom_id = result!!.id.toString()
                GlobalApplication.prefs.setString("custom_id", custom_id)
                Log.i("sangmin", "아이디 : ${custom_id}")
                Log.i("Log", "이메일 : ${result.kakaoAccount.email}")
                Log.i("Log", "성별 : ${result.kakaoAccount.gender}")
                Log.i("Log", "생일 : ${result.kakaoAccount.birthday}")
                Log.i("Log", "연령대 : ${result.kakaoAccount.ageRange}")

                checkNotNull(result) { "session response null" }
            }

        })
    }
}