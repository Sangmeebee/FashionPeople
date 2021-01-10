package com.sangmee.fashionpeople.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.repository.FUserRepository
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.ui.add.AddFragment
import com.sangmee.fashionpeople.ui.add.TagActivity
import com.sangmee.fashionpeople.ui.fragment.AlarmFragment
import com.sangmee.fashionpeople.ui.fragment.home.HomeFragment
import com.sangmee.fashionpeople.ui.fragment.info.InfoFragment
import com.sangmee.fashionpeople.ui.fragment.rank.RankFragment
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    private val fUserRepository: FUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }
    private val mainVm by viewModels<MainViewModel>()
    private val compositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainVm.getMySaveImage()

        navigationView.post { navigationView.selectedItemId = R.id.homeItem }
        navigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeItem -> {
                    if (currentFragment != "homeItem") {
                        replaceFragment(HomeFragment())
                        currentFragment = "homeItem"
                    }
                }
                R.id.rankItem -> {
                    if (currentFragment != "rankItem") {
                        replaceFragment(RankFragment())
                        currentFragment = "rankItem"
                    }
                }
                R.id.addItem -> {
                    fUserRepository.getFUser(
                        GlobalApplication.prefs.getString("${loginType}_custom_id", "")
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ user ->
                            user.evaluateNow?.let { evaluateNow ->
                                if (evaluateNow) {
                                    if (currentFragment != "addItem") {
                                        replaceFragment(AddFragment())
                                        currentFragment = "addItem"
                                    }
                                } else {
                                    addPhoto()
                                }
                            }
                        } , { t ->
                            Log.e("CALL_PROFILE_ERROR", t.message.toString())
                        }).addTo(compositeDisposable)
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
        for (i in 0..supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack();
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment).commit()
    }

    //fragment 교체(백스택 사용)
    fun replaceFragmentUseBackStack(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.frameLayout, fragment).commit()
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

    override fun onDestroy() {
        compositeDisposable.clear()
        mainVm.unBindViewModel()
        super.onDestroy()
    }

    companion object {
        private var currentFragment = ""
        private const val CHOOSE_PROFILE_IMG = 200
    }
}
