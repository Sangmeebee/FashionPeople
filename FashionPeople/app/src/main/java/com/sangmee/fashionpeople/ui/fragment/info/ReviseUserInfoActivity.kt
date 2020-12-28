package com.sangmee.fashionpeople.ui.fragment.info

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.jakewharton.rxbinding4.widget.textChanges
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.repository.FUserRepository
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import com.sangmee.fashionpeople.databinding.ActivityReviseUserInfoBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit


class ReviseUserInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviseUserInfoBinding
    private lateinit var customId: String
    private var gender = "남"
    private val fUserRepository: FUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_revise_user_info)
        customId = intent.getStringExtra("custom_id")!!
        binding.customId = customId
        binding.activity = this

        checkFillInTheBlanks()

        binding.rgGender.setOnCheckedChangeListener { group, checkedId ->
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

    //회원정보 저장(retrofit2)
    fun saveUser(customId: String) {
        //닉네임
        val name = binding.etNickname.text.toString()
        //소개 글
        val introduce = binding.etIntroduce.text.toString()

        fUserRepository.addUser(
            FUser(
                customId,
                name,
                introduce,
                gender,
                null,
                false,
                listOf(),
                listOf()
            ), {
                finish()
            }, { Log.e("sangmin_error", it) }
        )
    }

    private fun checkFillInTheBlanks() {
        binding.etNickname.textChanges()
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNullOrEmpty()) {
                    binding.btnComplete.background =
                        ContextCompat.getDrawable(this, R.drawable.bg_round_disable)
                    binding.tvAlert.visibility = View.VISIBLE
                    binding.btnComplete.isEnabled = false
                } else {
                    binding.btnComplete.background =
                        ContextCompat.getDrawable(this, R.drawable.bg_round)
                    binding.tvAlert.visibility = View.INVISIBLE
                    binding.btnComplete.isEnabled = true
                }
            }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
