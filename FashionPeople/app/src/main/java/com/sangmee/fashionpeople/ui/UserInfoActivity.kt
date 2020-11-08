package com.sangmee.fashionpeople.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import com.sangmee.fashionpeople.retrofit.RetrofitClient
import com.sangmee.fashionpeople.retrofit.model.FUser
import com.sangmee.fashionpeople.retrofit.model.Image
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class UserInfoActivity : AppCompatActivity() {

    val pref = GlobalApplication.prefs
    lateinit var imagePath: String
    var file: File? = null
    lateinit var customId: String
    val CHOOSE_PROFILEIMG = 200


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        //툴바 세팅
        setSupportActionBar(app_toolbar)
        val actionBar = supportActionBar!!
        actionBar.setDisplayShowTitleEnabled(false)
        toolbar_title.setText("정보 입력")
        actionBar.setDisplayHomeAsUpEnabled(true)


        //프로필 사진 선택
        profile_image.setOnClickListener {
            //외부 쓰기 퍼미션이 있다면
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                try {
                    //갤러리 앱 실행
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                    startActivityForResult(intent, CHOOSE_PROFILEIMG)

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    100
                )
            }
        }

        //버튼 클릭 (가입완료)
        authorization_btn.setOnClickListener {

            //회원 아이디
            customId = pref.getString("custom_id", "empty")
            //닉네임
            var name: String? = nickname.text.toString()
            //소개 글
            var instagramId: String? = instagram_id.text.toString()
            //프로필 사진
            var profileImage: String? = file?.name
            if (profileImage != null) {
                uploadWithTransferUtility(profileImage, file)
            }

            //회원정보 저장(retrofit2)
            RetrofitClient().getFUserService().addUser(
                FUser(
                    customId,
                    listOf(),
                    name,
                    instagramId,
                    profileImage
                )
            ).enqueue(object : Callback<FUser> {
                override fun onFailure(call: retrofit2.Call<FUser>, t: Throwable) {
                    Log.d("sangmin_error", t.message)
                }

                override fun onResponse(call: retrofit2.Call<FUser>, response: Response<FUser>) {
                    Log.d("sangmin_success", response.body().toString())
                    redirctUserInfoActivity()
                }
            })

        }
    }

    //툴바 뒤로가기 버튼
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            android.R.id.home -> { //toolbar의 back키 눌렀을 때 동작
                //preference에 저장된 아이디 제거
                pref.remove("custom_id")
                Log.d("sangmincheck", pref.getString("custom_id", "empty"))
                finish()
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //이미지Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    fun getRealPathFromUri(uri: Uri): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor = contentResolver.query(uri, proj, null, null, null)!!
        var index = c!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()

        var result = c.getString(index)

        return result
    }

    //뒤로가기 버튼 누를 시 custom_id 삭제
    override fun onBackPressed() {
        super.onBackPressed()
        pref.remove("custom_id")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "외부 메모리 읽기/쓰기 사용 가능", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "외부 메모리 읽기/쓰기 제한", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_PROFILEIMG) {
            if (resultCode == RESULT_OK) {
                try {
                    val uri: Uri = data!!.data!!
                    profile_image.setImageURI(uri)
                    imagePath = getRealPathFromUri(uri)
                    file = File(imagePath)
                    Log.d("sangmin-url", imagePath)
                } catch (e: Exception) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }

    }

    //aws s3에 이미지 업로드
    fun uploadWithTransferUtility(fileName: String, file: File?) {

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

        val uploadObserver = transferUtility.upload(
            "users/${customId}/profile/${fileName}",
            file,
            CannedAccessControlList.PublicRead
        )

        //CannedAccessControlList.PublicRead 읽기 권한 추가

        // Attach a listener to the observer
        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    Log.d("MYTAG", "fileupload")
                    redirctUserInfoActivity()

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

    fun redirctUserInfoActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


}
