package com.sangmee.fashionpeople.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.ActivitySettingBinding
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import com.sangmee.fashionpeople.kakaologin.PreferenceUtil

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var customId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        binding.activity = this@SettingActivity

        customId = GlobalApplication.prefs.getString("custom_id", "empty")

        binding.tvLogout.setOnClickListener {
            AlertDialog.Builder(this).setMessage(R.string.ask_logout_text)
                .setPositiveButton("네") { dialog, which ->
                    UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
                        override fun onCompleteLogout() {
                            GlobalApplication.prefs.remove(customId)
                            setResult(Activity.RESULT_OK)
                            this@SettingActivity.finish()
                        }
                    })

                }
                .setNegativeButton("아니오") { dialog, which ->

                }.create().show()
        }


    }


}