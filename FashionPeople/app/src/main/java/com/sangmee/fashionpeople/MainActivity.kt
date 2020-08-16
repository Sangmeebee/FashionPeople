package com.sangmee.fashionpeople

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.multidex.BuildConfig
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.sangmee.fashionpeople.fragment.*
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import com.sangmee.fashionpeople.kakaologin.UserInfoActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var dialog : LoginDialog
    //카카오 로그인 callback
    private var callback: SessionCallback = SessionCallback()
    lateinit var filepath : File
    companion object {
        private val CameraResult = 1111
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeItem -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, HomeFragment()).commit()
                }
                R.id.searchItem -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, SearchFragment()).commit()
                }
                R.id.addItem -> {

                    startCameraApp()

                }
                R.id.alarmItem -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, AlarmFragment()).commit()
                }
                R.id.infoItem -> {
                    val customId = GlobalApplication.prefs.getString("custom_id", "empty")
                    //다르다면(로그인이 안되어 있는 상태라면 로그인하라는 알림창)
                    if (customId=="empty") {
                        dialog = LoginDialog(this, "로그인을 해주세요", kakaoBtnListener)
                        dialog.show()
                    }
                    //sharedpreference에 있는 id와 DB에 있는 id가 같다면 info fragment 띄어준다
                    else {

                        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, InfoFragment()).commit()
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
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this)
        dialog.dismiss()

    }

    //카카오 로그인
    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //카카오톡 로그인 결과
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.i("Log", "session get current session")
            return
        }
        //카메라 앱 실행 후 결과
        if(requestCode== CameraResult && resultCode== RESULT_OK){
            if(filepath != null){
                val options: BitmapFactory.Options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //카카오톡 로그인 콜백
    inner class SessionCallback : ISessionCallback {
        lateinit var custom_id : String
        override fun onSessionOpenFailed(exception: KakaoException?) {
            Log.e("sangmin", "Session Call back :: onSessionOpenFailed: ${exception?.message}")
        }

        override fun onSessionOpened() {
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {

                override fun onFailure(errorResult: ErrorResult?) {
                    Log.i("sangmin", "Session Call back :: on failed ${errorResult?.errorMessage}")
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    Log.i("sangmin", "Session Call back :: onSessionClosed ${errorResult?.errorMessage}")

                }

                override fun onSuccess(result: MeV2Response?) {
                    Log.d("sangmin", "연결 성공")
                    custom_id = result!!.id.toString()
                    GlobalApplication.prefs.setString("custom_id", custom_id)
                    Log.i("sangmin", "아이디 : ${custom_id}")
                    Log.i("Log", "이메일 : ${result.kakaoAccount.email}")
                    Log.i("Log", "성별 : ${result.kakaoAccount.gender}")
                    Log.i("Log", "생일 : ${result.kakaoAccount.birthday}")
                    Log.i("Log", "연령대 : ${result.kakaoAccount.ageRange}")

                    checkNotNull(result) { "session response null" }
                    redirctUserInfoActivity()


                }

            })
        }
    }
    //화면전환 메소드
    fun redirctUserInfoActivity(){
        val intent = Intent(this, UserInfoActivity::class.java)
        startActivity(intent)
    }

    //퍼미션 확인후 카메라 앱 실행
    fun startCameraApp(){
        //외부 쓰기 퍼미션이 있다면
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            try {
                //디렉토리 생성
                val dirPath = Environment.getExternalStorageDirectory().absolutePath+"l/fashionPeope"
                val dir: File = File(dirPath)
                if(!dir.exists()){
                    dir.mkdir()
                    Log.d("sangmin", "make directory")
                }

                //파일 생성
                filepath = File.createTempFile("IMG", ".jpg", dir)
                if(!filepath.exists()){
                    filepath.createNewFile()
                    Log.d("sangmin", "make file")
                }
                //카메라 식별자로 intent 보냄
                val photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".provider", filepath)
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, CameraResult)
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        }
    }

}