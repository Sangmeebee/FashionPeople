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
import com.sangmee.fashionpeople.databinding.ActivityEmailLoginBinding
import com.sangmee.fashionpeople.ui.MainActivity

class EmailLoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityEmailLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_email_login)
        binding.activity = this
        binding.findId = R.string.find_info
        binding.signInId = R.string.sign_in
    }

    fun clickLoginBtn() {
        //Todo retrofit에서 아이디체크로 바꾸기
        auth.signInWithEmailAndPassword(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    GlobalApplication.prefs.setString("email_custom_id", binding.etEmail.text.toString())
                    Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Sangmeebee", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, task.exception?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun clickSignUpBtn() {
        val intent = Intent(this, EmailSignInActivity::class.java)
        startActivity(intent)
    }

    fun clickFindInfoBtn() {
    }
}
