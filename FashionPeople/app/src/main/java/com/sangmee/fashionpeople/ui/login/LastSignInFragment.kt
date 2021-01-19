package com.sangmee.fashionpeople.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.activity_user_info.et_introduce
import kotlinx.android.synthetic.main.activity_user_info.et_nickname
import kotlinx.android.synthetic.main.last_sign_in_fragment.*
import java.util.concurrent.TimeUnit


class LastSignInFragment : Fragment() {

    private val vm by activityViewModels<LoginViewModel>()
    private lateinit var binding: LastSignInFragmentBinding
    private var gender = "남"
    private val fUserRepository: FUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }
    private val compositeDisposable = CompositeDisposable()

    override fun onResume() {
        super.onResume()
        checkFillInTheBlanks()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //updateUI(currentUser)
        return inflater.inflate(R.layout.last_sign_in_fragment, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.vm = vm
            binding.gender = "남자"
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
                    binding.gender = "남자"
                    binding.tvGender.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.blue
                        )
                    )
                }
                R.id.rb_woman -> {
                    gender = "여"
                    binding.gender = "여자"
                    binding.tvGender.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.pink
                        )
                    )
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

        vm.isExist.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.btnComplete.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_round_disable)
                binding.tvAlert.visibility = View.VISIBLE
                binding.alert = "이미 존재하는 닉네임 입니다."
                binding.btnComplete.isEnabled = false
            } else {
                binding.btnComplete.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_round)
                binding.tvAlert.visibility = View.INVISIBLE
                binding.btnComplete.isEnabled = true
            }
        })
    }


    private fun checkFillInTheBlanks() {
        binding.etNickname.textChanges()
            .debounce(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNullOrEmpty()) {
                    binding.btnComplete.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_round_disable)
                    binding.tvAlert.visibility = View.VISIBLE
                    binding.alert = "닉네임을 입력해주세요"
                    binding.btnComplete.isEnabled = false
                } else {
                    vm.checkIsEigenvalue(it.toString())
                }
            }
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
                var height = 0
                val heightStr = et_height.text.toString()
                if (!heightStr.isNullOrEmpty()) {
                    height = heightStr.toInt()
                }
                var weight = 0
                val weightStr = et_weight.text.toString()
                if (!weightStr.isNullOrEmpty()) {
                    weight = weightStr.toInt()
                }
                fUserRepository.addUser(
                    FUser(
                        vm.customId.value.toString(),
                        name,
                        introduce,
                        gender,
                        height,
                        weight,
                        null,
                        listOf(),
                        listOf()
                    ), {
                        requireActivity().finish()
                    }, { e -> Log.e("sangmin_error", e) }
                )
            }
        }
    }

    override fun onPause() {
        compositeDisposable.clear()
        vm.unbindViewModel()
        super.onPause()
    }
}
