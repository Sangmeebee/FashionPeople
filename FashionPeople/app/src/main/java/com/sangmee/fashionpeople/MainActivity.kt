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
    lateinit var mCurrentPhotoPath: String
    companion object {
        private val CAMERA_START = 1111
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

                    //로그인이 안되어있으면 로그인 권유 다이얼로그 출력
                    val customId = GlobalApplication.prefs.getString("custom_id", "empty")
                    if(customId == "empty"){
                        dialog = LoginDialog(this, "로그인을 해주세요", kakaoBtnListener)
                        dialog.show()
                    }
                    //로그인이 되면 카메라 앱으로 전환
                    else{
                        startCameraApp()
                    }

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
        if(requestCode== CAMERA_START){
            Log.i("REQUEST_TAKE_PHOTO", "${Activity.RESULT_OK}" + " " + "${resultCode}")
            if (resultCode == RESULT_OK) {
                try {
                    galleryAddPic()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, HomeFragment()).commit()
                } catch (e: Exception) {
                    Log.e("REQUEST_TAKE_PHOTO", e.toString())
                }

            } else {
                Toast.makeText(this@MainActivity, "사진찍기를 취소하였습니다.", Toast.LENGTH_SHORT).show()
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

    //카메라 앱 실행
    fun startCameraApp(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            try {
                captureCamera()
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        }
    }

    //임시 파일 생성
    @Throws(IOException::class)
    fun createImageFile(): File? { // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_$timeStamp.jpg"
        var imageFile: File? = null
        val storageDir = File(
            Environment.getExternalStorageDirectory().toString() + "/Pictures",
            "FashionPeople"
        )
        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString())
            storageDir.mkdirs()
        }
        imageFile = File(storageDir, imageFileName)
        mCurrentPhotoPath = imageFile.absolutePath
        Log.d("sangmin", mCurrentPhotoPath)
        return imageFile
    }

    //카메라 실행
    private fun captureCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {

            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                Log.e("captureCamera Error", ex.toString())
                return
            }
            if (photoFile != null) { // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함
                val providerURI = FileProvider.getUriForFile(this, "$packageName.provider", photoFile)
                Log.d("sangmin_photoFile", photoFile.absolutePath)
                // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI)
                startActivityForResult(takePictureIntent, CAMERA_START)
            }
        }
    }

    //이미지 로컬 폴더에 저장
    private fun galleryAddPic() {
        Log.i("galleryAddPic", "Call");
        val mediaScanIntent: Intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        val f: File = File(mCurrentPhotoPath)
        val contentUri: Uri = Uri.fromFile(f)
        mediaScanIntent.setData(contentUri)
        sendBroadcast(mediaScanIntent)
        val customId = GlobalApplication.prefs.getString("custom_id", "empty")
        uploadWithTransferUtility(customId, f.name, f)

        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }

    //aws s3에 이미지 업로드
    fun uploadWithTransferUtility(customId: String, fileName: String, file: File?) {

        val credentialsProvider = CognitoCachingCredentialsProvider(
            applicationContext,
            "ap-northeast-2:04a21c16-627a-49a9-8229-f1c412ddebfa",  // 자격 증명 풀 ID
            Regions.AP_NORTHEAST_2 // 리전
        )

        TransferNetworkLossHandler.getInstance(applicationContext)

        val transferUtility = TransferUtility.builder()
            .context(applicationContext)
            .defaultBucket("fashionprofile-images")
            .s3Client(AmazonS3Client(credentialsProvider, Region.getRegion(Regions.AP_NORTHEAST_2)))
            .build()

        /* Store the new created Image file path */

        val uploadObserver = transferUtility.upload("users/${customId}/feed/${fileName}", file, CannedAccessControlList.PublicRead)

        //CannedAccessControlList.PublicRead 읽기 권한 추가

        // Attach a listener to the observer
        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    Log.d("MYTAG", "fileupload")
                }
            }

            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                val done = (((current.toDouble() / total) * 100.0).toInt())
                Log.d("MYTAG", "UPLOAD - - ID: $id, percent done = $done")
            }

            override fun onError(id: Int, ex: Exception) {
                Log.d("MYTAG", "UPLOAD ERROR - - ID: $id - - EX: ${ex.message.toString()}")
            }
        })

        // If you prefer to long-poll for updates
        if (uploadObserver.state == TransferState.COMPLETED) {
            /* Handle completion */

        }
    }

}