package com.sangmee.fashionpeople.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({

            val loginType = GlobalApplication.prefs.getString("login_type", "empty")
            if (loginType == "empty") {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val customId = GlobalApplication.prefs.getString("${loginType}_custom_id", "empty")
                if (customId == "empty") {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 1000)
    }
}
