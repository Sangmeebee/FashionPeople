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
import com.google.firebase.auth.FirebaseAuth
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.ThirdConfirmSignInFragmentBinding
import com.sangmee.fashionpeople.observer.LoginViewModel


class ThirdConfirmSignInFragment : Fragment() {

    private val vm by activityViewModels<LoginViewModel>()
    private lateinit var binding: ThirdConfirmSignInFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //updateUI(currentUser)
        return inflater.inflate(R.layout.third_confirm_sign_in_fragment, container, false)?.apply {
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
            FirebaseAuth.getInstance().currentUser?.let { fUser ->
                fUser.sendEmailVerification().addOnSuccessListener {
                    Toast.makeText(context, "메일을 재전송했습니다", Toast.LENGTH_SHORT).show()
                    vm.loadingSubject.onNext(false)
                    (activity as EmailSignInActivity).replaceFragmentUseBackStack(
                        ThirdConfirmSignInFragment()
                    )
                }.addOnFailureListener {
                    Toast.makeText(context, "이미 전송된 메일이 있습니다. 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        vm.nextBtnEvent.observe(this, Observer {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                vm.customId.value.toString(), vm.password.value.toString()
            ).addOnCompleteListener {
                val user = FirebaseAuth.getInstance().currentUser!!
                if (!user.isEmailVerified) {
                    Toast.makeText(context, R.string.authentication_mail, Toast.LENGTH_SHORT).show()
                } else {
                    (activity as EmailSignInActivity).replaceFragmentUseBackStack(LastSignInFragment())
                }
            }
        })


        vm.backBtnEvent.observe(this, Observer {
            FirebaseAuth.getInstance().currentUser?.delete()
            val fragmentManager: FragmentManager =
                (activity as EmailSignInActivity).supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()
            fragmentManager.popBackStack()
        })
    }
}
