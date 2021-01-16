package com.sangmee.fashionpeople.ui.fragment.info

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding4.widget.textChanges
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.databinding.ActivityReviseUserInfoBinding
import com.sangmee.fashionpeople.observer.ReviseInfoViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit


class ReviseUserInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviseUserInfoBinding
    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    val customId by lazy { GlobalApplication.prefs.getString("${loginType}_custom_id", "") }
    private lateinit var nickName: String
    private lateinit var gender: String
    private var introduce: String? = null
    private var profileImageName: String? = null

    private val vm by viewModels<ReviseInfoViewModel>()
    private val compositeDisposable = CompositeDisposable()

    override fun onResume() {
        super.onResume()
        checkFillInTheBlanks()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_revise_user_info)
        nickName = intent.getStringExtra("nick_name")!!
        gender = intent.getStringExtra("gender")!!
        intent.getStringExtra("profile_image_name")?.let {
            profileImageName = it
        }
        intent.getStringExtra("introduce")?.let {
            introduce = it
        }
        binding.customId = customId
        binding.nickName = nickName
        binding.isChecked = gender == "남"
        binding.gender = "남자"
        if (!introduce.isNullOrEmpty()) {
            binding.etIntroduce.apply {
                setText(introduce)
                setSelection(this.text.length)
            }
        }
        binding.activity = this

        binding.rgGender.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_man -> {
                    gender = "남"
                    binding.gender = "남자"
                    binding.tvGender.setTextColor(ContextCompat.getColor(this, R.color.blue))
                }
                R.id.rb_woman -> {
                    gender = "여"
                    binding.gender = "여자"
                    binding.tvGender.setTextColor(ContextCompat.getColor(this, R.color.pink))
                }
            }
        }

        bindViewModel()
    }

    //회원정보 저장(retrofit2)
    fun updateUser(customId: String) {
        //닉네임
        val name = binding.etNickname.text.toString()
        //소개 글
        binding.etIntroduce.text?.let { introduce = it.toString() }

        val fUser = FUser(customId, name, introduce, gender, profileImageName, null, null, null)
        vm.updateProfile(customId, fUser)
    }

    private fun checkFillInTheBlanks() {
        binding.etNickname.textChanges()
            .debounce(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNullOrEmpty()) {
                    binding.btnComplete.background =
                        ContextCompat.getDrawable(this, R.drawable.bg_round_disable)
                    binding.tvAlert.visibility = View.VISIBLE
                    binding.alert = "닉네임을 입력해주세요"
                    binding.btnComplete.isEnabled = false
                } else {
                    vm.checkIsEigenvalue(it.toString())
                }
            }
    }

    private fun finishThisActivity() {
        val name = binding.etNickname.text.toString()
        //소개 글
        binding.etIntroduce.text?.let { introduce = it.toString() }
        val intent = Intent()
        intent.putExtra("nick_name", name)
        intent.putExtra("gender", gender)
        intent.putExtra("introduce", introduce)
        Toast.makeText(
            applicationContext,
            "회원정보를 수정했습니다.",
            Toast.LENGTH_SHORT
        ).show()
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun bindViewModel() {

        vm.isExist.observe(this, Observer {
            if (it && binding.etNickname.text.toString() != nickName) {
                binding.btnComplete.background =
                    ContextCompat.getDrawable(this, R.drawable.bg_round_disable)
                binding.tvAlert.visibility = View.VISIBLE
                binding.alert = "이미 존재하는 닉네임 입니다."
                binding.btnComplete.isEnabled = false
            } else {
                binding.btnComplete.background =
                    ContextCompat.getDrawable(this, R.drawable.bg_round)
                binding.tvAlert.visibility = View.INVISIBLE
                binding.btnComplete.isEnabled = true
            }
        })

        vm.loadingSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.pbLoading.isVisible = it
                if (it) {
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            }
            .addTo(compositeDisposable)

        vm.finishSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { finishThisActivity() }
            .addTo(compositeDisposable)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onPause() {
        unbindViewModel()
        super.onPause()
    }

    private fun unbindViewModel() {
        compositeDisposable.clear()
        vm.unbindViewModel()
    }
}
