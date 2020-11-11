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
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.S3RemoteDataSource
import com.sangmee.fashionpeople.data.dataSource.remote.S3RemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.repository.FUserRepository
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File


class UserInfoActivity : AppCompatActivity() {

    private val pref = GlobalApplication.prefs
    lateinit var imagePath: String
    private var file: File? = null
    private val customId by lazy { pref.getString("custom_id", "empty") }
    private val CHOOSE_PROFILEIMG = 200
    private val FUserRepository: FUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }
    private val s3RemoteDataSource: S3RemoteDataSource by lazy {
        S3RemoteDataSourceImpl(
            applicationContext,
            customId
        )
    }

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
            saveUser(customId)
        }
    }

    //회원정보 저장(retrofit2)
    private fun saveUser(customId: String) {
        //닉네임
        val name = nickname.text.toString()
        //소개 글
        val instagramId = instagram_id.text.toString()
        //프로필 사진
        val profileImage = file?.name
        profileImage?.let {
            s3RemoteDataSource.uploadWithTransferUtility(it, file, "profile")
        }

        FUserRepository.addUser(
            FUser(
                customId,
                name,
                instagramId,
                profileImage,
                listOf()
            ), { redirectUserInfoActivity() }, { Log.e("sangmin_error", it) }
        )
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
    private fun getRealPathFromUri(uri: Uri): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor = contentResolver.query(uri, proj, null, null, null)!!
        val index = c!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
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

    private fun redirectUserInfoActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
