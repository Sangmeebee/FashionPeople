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
        val id: String? = binding.etEmail.text.toString()
        val pw: String? = binding.etPassword.text.toString()
        if (id.isNullOrEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
        } else {
            if (pw.isNullOrEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                ).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        GlobalApplication.prefs.setString(
                            "email_custom_id",
                            binding.etEmail.text.toString()
                        )
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Sangmeebee", "signInWithEmail:failure", task.exception)
                        val message = task.exception?.message.toString()
                        if("There is no user" in message) {
                            Toast.makeText(this, "이메일이 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                        }
                        if("The password is invalid" in message) {
                            Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }

    fun clickSignUpBtn() {
        val intent = Intent(this, EmailSignInActivity::class.java)
        startActivity(intent)
    }

    fun clickFindInfoBtn() {
        val intent = Intent(this, FindPasswordActivity::class.java)
        startActivity(intent)
    }
}
