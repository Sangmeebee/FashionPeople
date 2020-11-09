package com.sangmee.fashionpeople.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.ActivitySettingBinding
import com.sangmee.fashionpeople.data.GlobalApplication

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var customId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        binding.activity = this@SettingActivity
        setToolbar(binding.tbSetting)
        customId = GlobalApplication.prefs.getString("custom_id", "empty")

        initView()


    }

    private fun initView() {
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

    private fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow)
            setDisplayShowTitleEnabled(false)
        }
    }

    fun sendToDeveloperEmail() {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(DEVELOPER_EMAIL1, DEVELOPER_EMAIL2))
        }.run {
            if (this.resolveActivity(packageManager) != null) {
                startActivity(this)
            }
        }
    }


    companion object {
        const val DEVELOPER_EMAIL1 = "slflfl12@naver.com"
        const val DEVELOPER_EMAIL2 = "sangmeebee@naver.com"
    }

}
