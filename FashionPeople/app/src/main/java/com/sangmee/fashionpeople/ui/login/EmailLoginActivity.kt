package com.sangmee.fashionpeople.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import com.sangmee.fashionpeople.databinding.ActivityEmailLoginBinding
import com.sangmee.fashionpeople.ui.MainActivity

class EmailLoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityEmailLoginBinding
    private lateinit var auth: FirebaseAuth
    private val fUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_email_login)
        binding.activity = this
        binding.findId = R.string.find_info
        binding.signInId = R.string.sign_in
    }

    fun clickLoginBtn() {

        var idExist = false
        var passwordExist = false
        fUserRepository.getAllFUser(success = {
            for (fUser in it) {
                if (fUser.id == binding.etEmail.text.toString()) {
                    idExist = true
                    if (fUser.password == binding.etPassword.text.toString()) {
                        passwordExist = true
                    }
                }
            }
            if (!idExist) {
                Toast.makeText(this, "해당하는 아이디가 없습니다", Toast.LENGTH_SHORT).show()
            } else {
                if (!passwordExist) {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                } else {
                    GlobalApplication.prefs.setString(
                        "email_custom_id",
                        binding.etEmail.text.toString()
                    )
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }, failed = { Log.e("fashionPeopleError", it) })
    }

    fun clickSignUpBtn() {
        val intent = Intent(this, EmailSignInActivity::class.java)
        startActivity(intent)
    }

    fun clickFindInfoBtn() {
    }
}
