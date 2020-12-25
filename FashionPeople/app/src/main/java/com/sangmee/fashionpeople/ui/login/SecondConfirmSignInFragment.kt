package com.sangmee.fashionpeople.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.SecondConfirmSignInFragmentBinding
import com.sangmee.fashionpeople.observer.LoginViewModel
import kotlinx.android.synthetic.main.second_confirm_sign_in_fragment.*

class SecondConfirmSignInFragment : Fragment() {

    private val vm by activityViewModels<LoginViewModel>()
    private lateinit var binding: SecondConfirmSignInFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.second_confirm_sign_in_fragment, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.vm = vm
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerCallback()
    }

    private fun observerCallback() {

        vm.nextBtnEvent.observe(this, Observer {
            if (vm.password.value.toString() == binding.etPasswordConfirm.text.toString()) {
                (activity as EmailSignInActivity).replaceFragmentUseBackStack(ThirdSignInFragment())
            } else {
                tv_alert.visibility = View.VISIBLE
            }
        })

        vm.backBtnEvent.observe(this, Observer {
            val fragmentManager: FragmentManager =
                (activity as EmailSignInActivity).supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()
            fragmentManager.popBackStack()
        })
    }
}
