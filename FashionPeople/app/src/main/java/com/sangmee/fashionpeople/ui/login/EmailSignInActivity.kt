package com.sangmee.fashionpeople.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.observer.LoginViewModel

class EmailSignInActivity : AppCompatActivity() {
    private val vm by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_sign_in)
        replaceFragmentUseBackStack(FirstSignInFragment { finish() })
    }

    //fragment 교체(백스택 사용)
    fun replaceFragmentUseBackStack(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.frameLayout, fragment).commit()
    }

    override fun onBackPressed() {
        val index = supportFragmentManager.backStackEntryCount-1
        if (index==0) {
            finish()
        } else {
            if(index==3){
                Toast.makeText(this, "안전한 회원가입을 위해, 이 화면에서는 뒤로가기 버튼을 누를 수 없습니다", Toast.LENGTH_SHORT).show()
            } else{
                supportFragmentManager.popBackStack()
            }
        }
    }
}
