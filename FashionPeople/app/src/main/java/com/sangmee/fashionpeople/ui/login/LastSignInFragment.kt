package com.sangmee.fashionpeople.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding4.widget.textChanges
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.repository.FUserRepository
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import com.sangmee.fashionpeople.databinding.LastSignInFragmentBinding
import com.sangmee.fashionpeople.observer.LoginViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.android.synthetic.main.activity_user_info.*
import java.util.concurrent.TimeUnit


class LastSignInFragment : Fragment() {

    private val vm by activityViewModels<LoginViewModel>()
    private lateinit var binding: LastSignInFragmentBinding
    private var gender = "남"
    private val fUserRepository: FUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //화면 세팅
        (activity as EmailSignInActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        //updateUI(currentUser)
        return inflater.inflate(R.layout.last_sign_in_fragment, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.vm = vm
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerCallback()
        binding.rgGender.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_man -> {
                    gender = "남"
                    Toast.makeText(context, "남자", Toast.LENGTH_SHORT).show()
                }
                R.id.rb_woman -> {
                    gender = "여"
                    Toast.makeText(context, "여자", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observerCallback() {

        vm.nextBtnEvent.observe(this, Observer {
            saveUser()
        })

        vm.backBtnEvent.observe(this, Observer {
            val fragmentManager: FragmentManager =
                (activity as EmailSignInActivity).supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()
            fragmentManager.popBackStack()
        })

        binding.etNickname.textChanges()
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNullOrEmpty()) {
                    binding.btnComplete.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_round_disable)
                    binding.tvAlert.visibility = View.VISIBLE
                    binding.btnComplete.isEnabled = false
                } else {
                    binding.btnComplete.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_round)
                    binding.tvAlert.visibility = View.INVISIBLE
                    binding.btnComplete.isEnabled = true
                }
            }.addTo(compositeDisposable)
    }

    //회원정보 저장(retrofit2)
    private fun saveUser() {
        FirebaseAuth.getInstance().currentUser?.let {
            if (!it.isEmailVerified) {
                Toast.makeText(
                    context,
                    resources.getText(R.string.authentication_mail),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //닉네임
                val name = et_nickname.text.toString()
                //소개 글
                val introduce = et_introduce.text.toString()

                fUserRepository.addUser(
                    FUser(
                        vm.customId.value.toString(),
                        vm.password.value.toString(),
                        name,
                        introduce,
                        gender,
                        null,
                        0,
                        0,
                        false
                    ), {
                        val intent = Intent(activity, EmailLoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }, { e -> Log.e("sangmin_error", e) }
                )
            }
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
