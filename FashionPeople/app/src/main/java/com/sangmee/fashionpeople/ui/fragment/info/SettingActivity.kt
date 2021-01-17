package com.sangmee.fashionpeople.ui.fragment.info

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.network.ApiErrorCode
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.databinding.ActivitySettingBinding
import com.sangmee.fashionpeople.observer.InfoViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

class SettingActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    private lateinit var binding: ActivitySettingBinding
    private lateinit var customId: String
    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    private val vm by lazy { InfoViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        binding.activity = this@SettingActivity
        setToolbar(binding.tbSetting)
        customId = GlobalApplication.prefs.getString("${loginType}_custom_id", "empty")

        initView()


    }

    private fun initView() {

        binding.tvWithdrawal.setOnClickListener {
            AlertDialog.Builder(this).setMessage(R.string.ask_withdrawal_text)
                .setPositiveButton("네") { dialog, which ->
                    if (loginType == "kakao") {
                        withdrawalAtKakao()
                    }
                    if (loginType == "email") {
                        FirebaseAuth.getInstance().currentUser?.delete()
                        vm.deleteUser(customId)
                        Toast.makeText(
                            applicationContext,
                            "회원탈퇴에 성공했습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        GlobalApplication.prefs.remove("${loginType}_custom_id")
                        GlobalApplication.prefs.remove("login_type")
                        setResult(Activity.RESULT_OK)
                        this@SettingActivity.finish()
                    }
                }
                .setNegativeButton("아니오") { dialog, which ->

                }.create().show()
        }

        binding.tvLogout.setOnClickListener {
            AlertDialog.Builder(this).setMessage(R.string.ask_logout_text)
                .setPositiveButton("네") { dialog, which ->
                    if (loginType == "kakao") {
                        UserManagement.getInstance()
                            .requestLogout(object : LogoutResponseCallback() {
                                override fun onCompleteLogout() {
                                }
                            })
                    }
                    if (loginType == "email") {
                        Firebase.auth.signOut()
                    }
                    Toast.makeText(
                        applicationContext,
                        "정상적으로 로그아웃되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    GlobalApplication.prefs.remove("${loginType}_custom_id")
                    GlobalApplication.prefs.remove("login_type")
                    setResult(Activity.RESULT_OK)
                    this@SettingActivity.finish()

                }
                .setNegativeButton("아니오") { dialog, which ->

                }.create().show()
        }

    }

    private fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.arrow_back)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun withdrawalAtKakao() {
        UserManagement.getInstance().requestUnlink(object : UnLinkResponseCallback() {
            override fun onSuccess(result: Long?) {
                vm.deleteUser(customId)
                Toast.makeText(
                    applicationContext,
                    "회원탈퇴에 성공했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
                GlobalApplication.prefs.remove("${loginType}_custom_id")
                GlobalApplication.prefs.remove("login_type")
                setResult(Activity.RESULT_OK)
                this@SettingActivity.finish()
            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
                Toast.makeText(
                    applicationContext,
                    "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(errorResult: ErrorResult?) {
                val result = errorResult?.errorCode
                if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
                    Toast.makeText(
                        applicationContext,
                        "네트워크 연결이 불안정합니다. 다시 시도해 주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "회원탈퇴에 실패했습니다. 다시 시도해 주세요.",
                        Toast.LENGTH_SHORT
                    ).show();
                }
            }
        })
    }

    fun sendToDeveloperEmail() {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(DEVELOPER_EMAIL1, DEVELOPER_EMAIL2))
        }.run {
            if (this.resolveActivity(packageManager) != null) {
                startActivity(this)
            }
        }
    }

    // 뒤로가기 버튼 눌렀을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onResume() {
        super.onResume()
        bindViewModel()
    }

    private fun bindViewModel() {
        vm.loadingSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { binding.pbLoading.isVisible = it }
            .addTo(compositeDisposable)
    }


    override fun onPause() {
        unbindViewModel()
        super.onPause()
    }

    private fun unbindViewModel() {
        compositeDisposable.clear()
        vm.unbindViewModel()
    }


    companion object {
        const val DEVELOPER_EMAIL1 = "slflfl12@naver.com"
        const val DEVELOPER_EMAIL2 = "apfhdznzl@gmail.com"
    }

}
