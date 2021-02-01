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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class LoginDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentLoginDialogBinding
    private var callback: SessionCallback = SessionCallback()
    private lateinit var customId: String
    private val fUserRepository: FUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }
    private val mainVm by activityViewModels<MainViewModel>()
    private val publishSubject = PublishSubject.create<String>()
    private val compositeDisposable = CompositeDisposable()

    //구글 로그인
    private val auth by lazy { Firebase.auth }
    private val gso by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //배경화면 클릭해도 dismiss 안되게 설정
        initCallBack()
        isCancelable = false
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
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

    fun clickGoogleLoginBtn() {
        publishSubject.onNext("google")
    }

    fun clickKakaoLoginBtn() {
        publishSubject.onNext("kakao")
    }

    fun clickCancelBtn() {
        dismiss()
        mainVm.tagName.value?.let { tag -> (activity as MainActivity).updateBottomMenu(tag) }
    }

    //화면전환 메소드
    private fun redirectUserInfoActivity(exist: Boolean, type: String) {
        dismiss()
        if (exist) {
            GlobalApplication.prefs.setString("login_type", type)
            GlobalApplication.prefs.setString("${type}_custom_id", customId)
            mainVm.userId = customId
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        } else {
            val intent =
                Intent(activity, UserInfoActivity::class.java)
                    .putExtra("custom_id", customId)
                    .putExtra("login_type", type)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //구글 로그인
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("Sangmeebee", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("Sangmeebee", "Google sign in failed", e)
            }
        }
    }

    //구글 로그인
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Sangmeebee", "signInWithCredential:success")
                    val user = auth.currentUser
                    val myId = user?.email
                    customId = myId.toString()
                    checkAndSave(customId, "google")
                } else {
                    Log.w("Sangmeebee", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun checkAndSave(myId: String, type: String) {
        // 데이터베이스에 아이디 이미 있는지 체크
        var exist = false
        fUserRepository.getAllFUser(success = {
            for (fUser in it) {
                if (fUser.id == myId) {
                    exist = true
                }
            }
            redirectUserInfoActivity(exist, type)
        }, failed = { Log.e("fashionPeopleError", it) })
    }

    private fun initCallBack() {
        publishSubject.throttleFirst(5000L, TimeUnit.MILLISECONDS)
            .subscribe {
                if (it == "kakao") {
                    Toast.makeText(context, "카카오톡으로 로그인합니다.", Toast.LENGTH_SHORT).show()
                    Session.getCurrentSession().addCallback(callback)
                    Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this)
                }
                if (it == "google") {
                    Toast.makeText(context, "구글로 로그인합니다.", Toast.LENGTH_SHORT).show()
                    val signInIntent = googleSignInClient.signInIntent
                    startActivityForResult(signInIntent, RC_SIGN_IN)
                }
            }.addTo(compositeDisposable)

    }

    //카카오 로그인
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        Session.getCurrentSession().removeCallback(callback)
    }

    companion object {
        private const val RC_SIGN_IN = 209
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
                    checkAndSave(customId, "kakao")
                }
            })
        }

    }
}

