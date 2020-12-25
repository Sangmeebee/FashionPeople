package com.sangmee.fashionpeople.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.sangmee.fashionpeople.databinding.ThirdSignInFragmentBinding
import com.sangmee.fashionpeople.observer.LoginViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo


class ThirdSignInFragment : Fragment() {

    private val vm by activityViewModels<LoginViewModel>()
    private lateinit var binding: ThirdSignInFragmentBinding
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
        val currentUser = auth.currentUser
        //updateUI(currentUser)
        return inflater.inflate(R.layout.third_sign_in_fragment, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.vm = vm
            binding.authenticationId = R.string.authentication
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerCallback()
    }

    private fun observerCallback() {

        vm.authBtnEvent.observe(this, Observer {
            vm.loadingSubject.onNext(true)
            createUserWithEmailAndPassword(
                vm.customId.value.toString(),
                vm.password.value.toString()
            )
        })

        vm.backBtnEvent.observe(this, Observer {
            val fragmentManager: FragmentManager =
                (activity as EmailSignInActivity).supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()
            fragmentManager.popBackStack()
        })

        vm.loadingSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { binding.pbLoading.isVisible = it }
            .addTo(compositeDisposable)

    }

    private fun createUserWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("Sangmeebee", "createUserWithEmail:success")
                val user = auth.currentUser
                user?.let { fUser ->
                    fUser.sendEmailVerification().addOnSuccessListener {
                        vm.loadingSubject.onNext(false)
                        (activity as EmailSignInActivity).replaceFragmentUseBackStack(ThirdConfirmSignInFragment())
                    }.addOnFailureListener { e ->
                        Toast.makeText(
                            context,
                            e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                //updateUI(user)
            } else {
                vm.loadingSubject.onNext(false)
                // If sign in fails, display a message to the user.
                Log.w("Sangmeebee", "createUserWithEmail:failure", it.exception)
                Toast.makeText(
                    context, it.exception?.message,
                    Toast.LENGTH_SHORT
                ).show()
                //updateUI(null)
            }
        }

    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}
