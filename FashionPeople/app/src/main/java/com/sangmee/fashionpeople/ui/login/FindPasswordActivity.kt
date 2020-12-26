package com.sangmee.fashionpeople.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sangmee.fashionpeople.R

class FindPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)
        replaceFragmentUseBackStack(FirstFindPasswordFragment { finish() })
    }

    //fragment 교체(백스택 사용)
    fun replaceFragmentUseBackStack(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.email_frame_layout, fragment).commit()
    }

    override fun onBackPressed() {
        val index = supportFragmentManager.backStackEntryCount - 1
        if (index == 0) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}
