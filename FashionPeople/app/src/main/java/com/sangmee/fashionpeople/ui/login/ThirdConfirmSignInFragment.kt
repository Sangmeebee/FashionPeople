package com.sangmee.fashionpeople.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.ThirdConfirmSignInFragmentBinding
import com.sangmee.fashionpeople.observer.LoginViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo


class ThirdConfirmSignInFragment : Fragment() {

    private val vm by activityViewModels<LoginViewModel>()
    private lateinit var binding: ThirdConfirmSignInFragmentBinding
    private lateinit var auth: FirebaseAuth
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

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
            vm.loadingSubject.onNext(true)
            createUserWithEmailAndPassword(
                vm.customId.value.toString(),
                vm.password.value.toString()
            )
        })

        vm.nextBtnEvent.observe(this, Observer {
            vm.isOk.value?.let {
                if(it){
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
                } else {
                    Toast.makeText(context, "메일을 확인하세요", Toast.LENGTH_SHORT).show()
                }
            }
        })


        vm.backBtnEvent.observe(this, Observer {
            if(vm.isOk.value!!){
                FirebaseAuth.getInstance().currentUser?.delete()
            }
            val fragmentManager: FragmentManager =
                (activity as EmailSignInActivity).supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()
            fragmentManager.popBackStack()
        })

        vm.loadingSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.pbLoading.isVisible = it
                if (it) {
                    requireActivity().window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                } else {
                    requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            }, {
                Log.e("Sangmeebee", it.message.toString())
            })
            .addTo(compositeDisposable)
    }

    private fun createUserWithEmailAndPassword(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("Sangmeebee", "createUserWithEmail:success")
                val user = auth.currentUser
                user?.let { fUser ->
                    fUser.sendEmailVerification().addOnSuccessListener {
                        Toast.makeText(context, "메일을 전송했습니다. 확인해주세요.", Toast.LENGTH_LONG).show()
                        vm.loadingSubject.onNext(false)
                        vm.isOk.value = true
                    }.addOnFailureListener { e ->
                        vm.loadingSubject.onNext(false)
                        Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                vm.loadingSubject.onNext(false)
                Toast.makeText(context, "이미 전송된 메일이 있습니다. 확인해주세요.", Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
