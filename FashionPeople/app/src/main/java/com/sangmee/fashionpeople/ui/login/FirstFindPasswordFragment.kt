package com.sangmee.fashionpeople.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jakewharton.rxbinding4.widget.textChanges
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.repository.FUserRepository
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import com.sangmee.fashionpeople.databinding.FirstFindPasswordFragmentBinding
import com.sangmee.fashionpeople.observer.LoginViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit

class FirstFindPasswordFragment(private val finishActivity: () -> Unit) : Fragment() {

    private val vm by activityViewModels<LoginViewModel>()
    private lateinit var binding: FirstFindPasswordFragmentBinding
    private val compositeDisposable = CompositeDisposable()
    private val fUserRepository: FUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.first_find_password_fragment, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.vm = vm
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerCallback()
        checkEmailForm()
    }

    private fun observerCallback() {

        vm.nextBtnEvent.observe(this, Observer {
            checkEmailExist()
        })

        vm.backBtnEvent.observe(this, Observer {
            finishActivity()
        })
    }

    private fun checkEmailForm() {
        //텍스트가 변할때 이메일 형식인지 체크
        binding.etEmail.textChanges()
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                try {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(it.toString())
                            .matches() && it.isNotEmpty()
                    ) {
                        binding.tvAlert.visibility = View.VISIBLE
                    } else {
                        binding.tvAlert.visibility = View.GONE
                    }
                } catch (e: IllegalStateException) {
                }

            }.addTo(compositeDisposable)
    }

    private fun checkEmailExist() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString())
                .matches()
        ) {
            Toast.makeText(context, "입력한 내용을 확인해주세요", Toast.LENGTH_SHORT).show()
        } else {
            var exist = false
            fUserRepository.getAllFUser({
                for (user in it) {
                    if (user.id == binding.etEmail.text.toString()) {
                        exist = true
                        vm.customId.value = user.id
                    }
                }
                if (exist) {
                    Firebase.auth.sendPasswordResetEmail(vm.customId.value.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Sangmeebee", "Email sent.")
                            }
                        }
                    (activity as FindPasswordActivity).replaceFragmentUseBackStack(
                        SecondFindPasswordFragment()
                    )
                } else {
                    Toast.makeText(context, "존재하지 않는 계정입니다", Toast.LENGTH_SHORT).show()
                }
            }, { Log.e("Sangmeebee", it) })
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}
