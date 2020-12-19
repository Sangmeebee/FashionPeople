package com.sangmee.fashionpeople.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.ActivityEmailLoginBinding

class EmailLoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityEmailLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_email_login)
        binding.activity = this
        binding.findId = R.string.find_info
        binding.signInId = R.string.sign_in
    }

    fun clickBackBtn() {
        finish()
    }


    fun clickLoginBtn() {
    }

    fun clickSignInBtn() {
        val intent = Intent(this, EmailSignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun clickFindInfoBtn() {

    }
}
