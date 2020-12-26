package com.sangmee.fashionpeople.ui.login

import android.app.Service
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding4.widget.textChanges
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.SecondSignInFragmentBinding
import com.sangmee.fashionpeople.observer.LoginViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.android.synthetic.main.first_sign_in_fragment.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class SecondSignInFragment : Fragment() {

    private val vm by activityViewModels<LoginViewModel>()
    private lateinit var binding: SecondSignInFragmentBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.second_sign_in_fragment, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.vm = vm
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerCallback()
        checkPasswordForm()
        showKeyBoard(binding.etPassword)
    }

    private fun observerCallback() {

        vm.nextBtnEvent.observe(this, Observer {

            if (!Pattern.matches(
                    "^(?=.*\\d)(?=.*[a-zA-Z]).{8,20}$",
                    binding.etPassword.text.toString()
                )
            ) {
                Toast.makeText(context, "입력하신 내용을 확인해주세요", Toast.LENGTH_SHORT).show()
            } else {
                vm.password.value = binding.etPassword.text.toString()
                (activity as EmailSignInActivity).replaceFragmentUseBackStack(
                    SecondConfirmSignInFragment()
                )
            }
        })

        vm.backBtnEvent.observe(this, Observer {
            val fragmentManager: FragmentManager =
                (activity as EmailSignInActivity).supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()
            fragmentManager.popBackStack()
        })
    }

    private fun checkPasswordForm() {
        //텍스트가 변할때 이메일 형식인지 체크
        binding.etPassword.textChanges()
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                try {
                    if (!Pattern.matches(
                            "^(?=.*\\d)(?=.*[a-zA-Z]).{8,20}$",
                            it.toString()
                        ) && it.isNotEmpty()
                    ) {
                        tv_alert.visibility = View.VISIBLE
                    } else {
                        tv_alert.visibility = View.GONE
                    }
                } catch (e: IllegalStateException) {
                }
            }.addTo(compositeDisposable)

    }

    private fun showKeyBoard(view: View) {
        view.requestFocus()
        val imm = requireContext().getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        view.postDelayed({
            imm.showSoftInput(view, 0)
        }, 30)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
