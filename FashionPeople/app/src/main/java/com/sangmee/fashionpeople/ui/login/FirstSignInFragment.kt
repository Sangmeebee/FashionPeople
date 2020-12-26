package com.sangmee.fashionpeople.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding4.widget.textChanges
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FirstSignInFragmentBinding
import com.sangmee.fashionpeople.observer.LoginViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.android.synthetic.main.first_sign_in_fragment.*
import java.util.concurrent.TimeUnit

class FirstSignInFragment(private val finishActivity: () -> Unit) : Fragment() {

    private val vm by activityViewModels<LoginViewModel>()
    private lateinit var binding: FirstSignInFragmentBinding
    private val compositeDisposable = CompositeDisposable()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.first_sign_in_fragment, container, false)?.apply {
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
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString())
                    .matches()
            ) {
                Toast.makeText(context, "입력한 내용을 확인해주세요", Toast.LENGTH_SHORT).show()
            } else {
                vm.customId.value = binding.etEmail.text.toString()
                (activity as EmailSignInActivity).replaceFragmentUseBackStack(SecondSignInFragment())
            }
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
                        tv_alert.visibility = View.VISIBLE
                    } else {
                        tv_alert.visibility = View.GONE
                    }
                } catch (e: IllegalStateException) {
                }

            }.addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}
