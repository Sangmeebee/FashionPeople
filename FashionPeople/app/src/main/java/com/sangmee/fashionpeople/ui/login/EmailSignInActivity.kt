package com.sangmee.fashionpeople.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.sangmee.fashionpeople.R

class EmailSignInActivity : AppCompatActivity() {

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
            if(index==4){
                FirebaseAuth.getInstance().currentUser?.delete()
            }
            supportFragmentManager.popBackStack()
        }
    }
}
