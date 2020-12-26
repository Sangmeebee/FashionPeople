package com.sangmee.fashionpeople.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.SecondFindPasswordFragmentBinding
import com.sangmee.fashionpeople.observer.LoginViewModel


class SecondFindPasswordFragment : Fragment() {

    private val vm by activityViewModels<LoginViewModel>()
    private lateinit var binding: SecondFindPasswordFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //updateUI(currentUser)
        return inflater.inflate(R.layout.second_find_password_fragment, container, false)?.apply {
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

        vm.emailBtnEvent.observe(this, Observer {
            Firebase.auth.sendPasswordResetEmail(vm.customId.value.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "메일을 재전송했습니다", Toast.LENGTH_SHORT).show()
                    }
                }
        })

        vm.nextBtnEvent.observe(this, Observer {
            requireActivity().finish()
        })


        vm.backBtnEvent.observe(this, Observer {
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()
            fragmentManager.popBackStack()
        })
    }
}
