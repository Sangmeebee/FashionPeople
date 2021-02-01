package com.sangmee.fashionpeople.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding4.widget.textChanges
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.repository.FUserRepository
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import com.sangmee.fashionpeople.databinding.ActivityUserInfoBinding
import com.sangmee.fashionpeople.ui.MainActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_user_info.*
import java.util.concurrent.TimeUnit


class UserInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserInfoBinding
    private lateinit var loginType: String
    private lateinit var customId: String
    private var gender = "남"
    private val vm by viewModels<UserInfoVIewModel>()
    private val fUserRepository: FUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }
    private val compositeDisposable = CompositeDisposable()

    override fun onResume() {
        super.onResume()
        checkFillInTheBlanks()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_info)
        loginType = intent.getStringExtra("login_type")!!
        customId = intent.getStringExtra("custom_id")!!
        binding.customId = customId
        binding.gender = "남자"
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

        initViewModel()
    }

    //회원정보 저장(retrofit2)
    fun saveUser(customId: String) {
        //닉네임
        val name = et_nickname.text.toString()
        //소개 글
        val introduce = et_introduce.text.toString()
        var height = 0
        val heightStr = et_height.text.toString()
        if (!heightStr.isNullOrEmpty()) {
            height = heightStr.toInt()
        }
        var weight = 0
        val weightStr = et_weight.text.toString()
        if (!weightStr.isNullOrEmpty()) {
            weight = weightStr.toInt()
        }

        fUserRepository.addUser(
            FUser(
                customId,
                name,
                introduce,
                gender,
                height,
                weight,
                null,
                listOf(),
                listOf()
            ), {
                GlobalApplication.prefs.setString("login_type", loginType)
                GlobalApplication.prefs.setString("${loginType}_custom_id", customId)
                val intent = Intent(this, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }, { errorMsg() }
        )
    }

    private fun errorMsg() {
        Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT).show()
    }

    private fun initViewModel() {
        vm.isExist.observe(this, Observer {
            if (it) {
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

    override fun onPause() {
        compositeDisposable.clear()
        vm.unbindViewModel()
        super.onPause()
    }
}
