package com.sangmee.fashionpeople.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.S3RemoteDataSource
import com.sangmee.fashionpeople.data.dataSource.remote.S3RemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.repository.FUserRepository
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import com.sangmee.fashionpeople.databinding.ActivityUserInfoBinding
import com.sangmee.fashionpeople.ui.MainActivity
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File


class UserInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserInfoBinding
    private lateinit var imagePath: String
    private var file: File? = null
    private lateinit var customId: String
    private var gender = "남"
    private val fUserRepository: FUserRepository by lazy {
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_info)
        customId = intent.getStringExtra("custom_id")!!
        binding.customId = customId
        binding.activity = this

        setToolbar()
        checkWrittenNeeds()

        rg_gender.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_man -> {
                    gender = "남"
                    Toast.makeText(this, "남자", Toast.LENGTH_SHORT).show()
                }
                R.id.rb_woman -> {
                    gender = "여"
                    Toast.makeText(this, "여자", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setToolbar() {
        setSupportActionBar(app_toolbar)
        val actionBar = supportActionBar!!
        actionBar.setDisplayShowTitleEnabled(false)
        toolbar_title.text = "정보 입력"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    //툴바 뒤로가기 버튼
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            android.R.id.home -> { //toolbar의 back키 눌렀을 때 동작
                finish()
            }
        }
        return super.onOptionsItemSelected(item!!);
    }

    private fun checkWrittenNeeds() {
        et_nickname.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                btn_complete.background =
                    ContextCompat.getDrawable(this, R.drawable.bg_round_disable)
            } else {
                btn_complete.background = ContextCompat.getDrawable(this, R.drawable.bg_round)
            }
        }
    }

    //회원정보 저장(retrofit2)
    fun saveUser(customId: String) {

        if (et_nickname.text.isEmpty()) {
            tv_alert.visibility = View.VISIBLE
        } else {
            //닉네임
            val name = et_nickname.text.toString()
            //소개 글
            val introduce = et_introduce.text.toString()
            //프로필 사진
            val profileImage = file?.name
            profileImage?.let {
                s3RemoteDataSource.uploadWithTransferUtility(it, file, "profile")
            }

            fUserRepository.addUser(
                FUser(
                    customId,
                    name,
                    introduce,
                    gender,
                    profileImage,
                    0,
                    0,
                    false
                ), {
                    GlobalApplication.prefs.setString("custom_id", customId)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }, { Log.e("sangmin_error", it) }
            )
        }
    }

    //이미지Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    private fun getRealPathFromUri(uri: Uri): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor = contentResolver.query(uri, proj, null, null, null)!!
        val index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()

        return c.getString(index)
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

    fun selectProfileImage() {
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

    companion object{
        private const val CHOOSE_PROFILEIMG = 200
    }
}
