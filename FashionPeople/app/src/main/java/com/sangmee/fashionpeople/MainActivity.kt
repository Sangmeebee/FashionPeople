package com.sangmee.fashionpeople

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.kakao.auth.AuthType
import com.kakao.auth.Session
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import com.sangmee.fashionpeople.kakaologin.SessionCallback
import com.sangmee.fashionpeople.kakaologin.UserInfoActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var dialog : MyDialog
    //카카오 로그인 callback
    private var callback: SessionCallback = SessionCallback()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeItem -> {

                }
                R.id.searchItem -> {

                }
                R.id.addItem -> {

                }

                R.id.alarmItem -> {

                }
                R.id.infoItem -> {
                    val customId = GlobalApplication.prefs.getString("custom_id", "empty")
                    //다르다면(로그인이 안되어 있는 상태라면 로그인하라는 알림창)
                    Log.d("sangmin_getSharedPre", customId)
                    if (customId=="empty") {
                        dialog = MyDialog(this, "로그인을 해주세요", kakaoBtnListener)
                        dialog.show()
                    }
                    //sharedpreference에 있는 id/pw와 DB에 있는 id/pw가 같다면 info fragment 띄어준다
                    else {
                        //수정해야한다.
                        dialog = MyDialog(this, "로그인을 해주세요", kakaoBtnListener)
                        dialog.show()
                    }

                }
            }
            return@setOnNavigationItemSelectedListener true

        }
    }

    //다이얼로그의 카카오로그인 버튼 리스터
    val kakaoBtnListener =View.OnClickListener{
        Toast.makeText(this, "카카오톡으로 로그인합니다.", Toast.LENGTH_SHORT).show()
        //카카오 콜백 추가
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().open(AuthType.KAKAO_TALK, this)
        val intent = Intent(this, UserInfoActivity::class.java)
        startActivity(intent)
        dialog.dismiss()

    }

    override fun onPause() {
        super.onPause()
    }

    //카카오 로그인
    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.i("Log", "session get current session")
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}