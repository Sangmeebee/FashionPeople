package com.sangmee.fashionpeople.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.model.stack.Stack
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.ui.add.TagActivity
import com.sangmee.fashionpeople.ui.fragment.home.HomeFragment
import com.sangmee.fashionpeople.ui.fragment.info.InfoFragment
import com.sangmee.fashionpeople.ui.fragment.rank.RankFragment
import com.sangmee.fashionpeople.ui.fragment.search.SearchFragment
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mainVm by viewModels<MainViewModel>()
    private val compositeDisposable = CompositeDisposable()
    private val behaviorSubject = BehaviorSubject.createDefault(0L)

    override fun onResume() {
        super.onResume()
        mainVm.getMySaveImage()
        mainVm.getUser()
        mainVm.tagName.value?.let { updateBottomMenu(it) }
        initExitCallback()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navigationView.post { navigationView.selectedItemId = R.id.homeItem }
        navigationView.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.homeItem -> {
                    changeFragment("home", HomeFragment(), mainVm.homeFragments)
                    setTagList("home")
                }
                R.id.rankItem -> {
                    changeFragment("rank", RankFragment(), mainVm.rankFragments)
                    setTagList("rank")
                }
                R.id.addItem -> {
                    addPhoto()
                }
                R.id.searchItem -> {
                    changeFragment("search", SearchFragment(), mainVm.searchFragments)
                    setTagList("search")
                }
                R.id.infoItem -> {
                    changeFragment("info", InfoFragment(), mainVm.infoFragments)
                    setTagList("info")
                }
            }
            true
        }
    }

    private fun setTagList(tag: String) {
        if (mainVm.tagList.contains(tag)) {
            mainVm.tagList.remove(tag)
        }
        mainVm.tagList.push(tag)
    }

    private fun changeFragment(
        tag: String,
        fragment: Fragment,
        fragments: Stack<Fragment>
    ) {
        if (mainVm.tagName.value != tag) {
            mainVm.tagName.value = tag
            if (fragments.isEmpty()) {
                fragments.push(fragment)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.add(R.id.frameLayout, fragment)
                transaction.commit()
            } else {
                showRecentHideOldFragment(fragments.peek(), null)
            }
        } else {
            val transaction = supportFragmentManager.beginTransaction()
            for(i in 0 until fragments.count()){
                transaction.remove(fragments.pop())
            }
            fragments.push(fragment)
            transaction.add(R.id.frameLayout, fragment)
            transaction.commit()
        }
    }

    private fun showRecentHideOldFragment(fragment: Fragment, oldFragment: Fragment?) {
        supportFragmentManager.beginTransaction().apply {
            oldFragment?.let { remove(it) }
            if (fragment.isAdded) {
                show(fragment)
            } else {
                add(R.id.frameLayout, fragment)
            }
            supportFragmentManager.fragments.forEach {
                if (it != fragment && it.isAdded) {
                    hide(it)
                }
            }
        }.commit()
    }

    //fragment 교체(백스택 사용)
    fun replaceFragmentUseTagBackStack(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frameLayout, fragment)
        transaction.commit()
        setFragmentStack(fragment, tag)
    }

    private fun setFragmentStack(fragment: Fragment, tag: String) {
        when (tag) {
            "home" -> mainVm.homeFragments.push(fragment)
            "rank" -> mainVm.rankFragments.push(fragment)
            "search" -> mainVm.searchFragments.push(fragment)
            "info" -> mainVm.infoFragments.push(fragment)
            else -> Log.e("Sangmeebee", "setFragmentStackError")
        }
    }

    override fun onBackPressed() {
        val map = mutableMapOf<String, Stack<Fragment>>()
        map["home"] = mainVm.homeFragments
        map["rank"] = mainVm.rankFragments
        map["search"] = mainVm.searchFragments
        map["info"] = mainVm.infoFragments

        var tag = mainVm.tagList.peek()
        if (mainVm.tagList.count() == 1 && map[tag]?.count() == 1) {
            behaviorSubject.onNext(System.currentTimeMillis())
        } else {
            var oldFragment: Fragment?
            if (map[tag]?.count() == 1) {
                oldFragment = map[tag]?.pop()
                mainVm.tagList.pop()
                tag = mainVm.tagList.peek()
            } else {
                oldFragment = map[tag]?.pop()
            }
            Log.d("Sangmeebee", tag)
            val fragment = map[tag]?.peek()!!
            showRecentHideOldFragment(fragment, oldFragment)
        }
        updateBottomMenu(tag)
    }

    private fun updateBottomMenu(tag: String) {

        if (tag == "home") {
            mainVm.tagName.value = "home"
            navigationView.menu.findItem(R.id.homeItem).isChecked = true
        }
        if (tag == "rank") {
            mainVm.tagName.value = "rank"
            navigationView.menu.findItem(R.id.rankItem).isChecked = true
        }
        if (tag == "search") {
            mainVm.tagName.value = "search"
            navigationView.menu.findItem(R.id.searchItem).isChecked = true
        }
        if (tag == "info") {
            mainVm.tagName.value = "info"
            navigationView.menu.findItem(R.id.infoItem).isChecked = true
        }

    }

    private fun initExitCallback() {
        behaviorSubject.buffer(2, 1)
            .map { it[0] to it[1] }
            .subscribe {
                if (it.second - it.first < 2000L) {
                    //앱 종료
                    moveTaskToBack(true)
                    if (Build.VERSION.SDK_INT >= 21) {
                        finishAndRemoveTask()
                    } else {
                        finish()
                    }
                    android.os.Process.killProcess(android.os.Process.myPid())
                } else {
                    Toast.makeText(this, "앱을 종료 하려면 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
                }
            }.addTo(compositeDisposable)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_PROFILE_IMG && resultCode == RESULT_OK && data != null) {
            try {
                val uri: Uri = data.data!!
                CropImage.activity(uri)
                    .setAspectRatio(3, 4)
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

    override fun onPause() {
        compositeDisposable.clear()
        mainVm.unBindViewModel()
        super.onPause()
    }

    companion object {
        private const val CHOOSE_PROFILE_IMG = 200
    }
}
