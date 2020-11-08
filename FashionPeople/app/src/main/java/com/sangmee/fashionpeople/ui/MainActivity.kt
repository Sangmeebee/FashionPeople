package com.sangmee.fashionpeople.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import com.sangmee.fashionpeople.ui.fragment.AlarmFragment
import com.sangmee.fashionpeople.ui.fragment.home.HomeFragment
import com.sangmee.fashionpeople.ui.fragment.InfoFragment
import com.sangmee.fashionpeople.ui.fragment.SearchFragment
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeItem -> {
                    if (currentFragment != "homeItem") {
                        replaceFragment(HomeFragment())
                        currentFragment = "homeItem"
                    }
                }
                R.id.searchItem -> {
                    if (currentFragment != "searchItem") {
                        replaceFragment(SearchFragment())
                        currentFragment = "searchItem"
                    }
                }
                R.id.addItem -> {
                    addPhoto()
                }

                R.id.alarmItem -> {
                    if (currentFragment != "alarmItem") {
                        replaceFragment(AlarmFragment())
                        currentFragment = "alarmItem"
                    }
                }
                R.id.infoItem -> {
                    if (currentFragment != "infoItem") {
                        replaceFragment(InfoFragment())
                        currentFragment = "infoItem"
                    }
                }

            }
            return@setOnNavigationItemSelectedListener true

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_PROFILE_IMG && resultCode == RESULT_OK && data != null) {
            try {
                val uri: Uri = data.data!!
                CropImage.activity(uri)
                    .setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
            } catch (e: Exception) {

            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                //TagFragment로 이동 & resultUri 전
                GlobalApplication.prefs.setString("resultUri", resultUri.toString())
                val intent = Intent(this, TagActivity::class.java)
                startActivity(intent)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e("TAG_ERROR", result.error.toString())
            }
        }
    }

    //fragment 교체
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment).commit()
    }

    //갤러리에서 사진 등록
    private fun addPhoto() {
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                //갤러리 앱 실행
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                startActivityForResult(intent, CHOOSE_PROFILE_IMG)

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


    companion object {
        private var currentFragment = "homeItem"
        private const val CHOOSE_PROFILE_IMG = 200
    }
}
