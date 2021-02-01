package com.sangmee.fashionpeople.ui.login

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.repository.FUserRepository
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import com.sangmee.fashionpeople.databinding.FragmentLoginDialogBinding
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.ui.MainActivity

class LoginDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentLoginDialogBinding
    private var callback: SessionCallback = SessionCallback()
    private lateinit var customId: String
    private val fUserRepository: FUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }
    private val mainVm by activityViewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //배경화면 클릭해도 dismiss 안되게 설정
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return inflater.inflate(R.layout.fragment_login_dialog, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.dialog = this@LoginDialogFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun clickKakaoLoginBtn() {
        Toast.makeText(context, "카카오톡으로 로그인합니다.", Toast.LENGTH_SHORT).show()
        //카카오 콜백 추가
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this)
    }

    fun clickCancelBtn() {
        dismiss()
        mainVm.tagName.value?.let { tag -> (activity as MainActivity).updateBottomMenu(tag) }
    }

    //화면전환 메소드
    private fun redirectUserInfoActivity(exist: Boolean) {
        dismiss()
        if (exist) {
            GlobalApplication.prefs.setString("login_type", "kakao")
            GlobalApplication.prefs.setString("kakao_custom_id", customId)
            mainVm.userId = customId
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            val intent =
                Intent(activity, UserInfoActivity::class.java).putExtra("custom_id", customId)
            startActivity(intent)
        }
    }

    //카카오 로그인
    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }


    //카카오톡 로그인 콜백
    inner class SessionCallback : ISessionCallback {
        lateinit var myId: String
        override fun onSessionOpenFailed(exception: KakaoException?) {
            Log.e("Sangmeebee", "Session Call back :: onSessionOpenFailed: ${exception?.message}")
        }

        override fun onSessionOpened() {
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {

                override fun onFailure(errorResult: ErrorResult?) {
                    Log.i(
                        "Sangmeebee",
                        "Session Call back :: on failed ${errorResult?.errorMessage}"
                    )
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    Log.i(
                        "Sangmeebee",
                        "Session Call back :: onSessionClosed ${errorResult?.errorMessage}"
                    )
                }

                override fun onSuccess(result: MeV2Response?) {
                    Log.d("sangmin", "연결 성공")
                    myId = result!!.id.toString()
                    customId = myId

                    // 데이터베이스에 아이디 이미 있는지 체크
                    var exist = false
                    fUserRepository.getAllFUser(success = {
                        for (fUser in it) {
                            if (fUser.id == myId) {
                                exist = true
                            }
                        }
                        redirectUserInfoActivity(exist)
                    }, failed = { Log.e("fashionPeopleError", it) })
                }
            })
        }

    }
}

