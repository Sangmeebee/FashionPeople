package com.sangmee.fashionpeople.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.S3RemoteDataSource
import com.sangmee.fashionpeople.data.dataSource.remote.S3RemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.data.repository.FUserRepository
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import com.sangmee.fashionpeople.databinding.LastSignInFragmentBinding
import com.sangmee.fashionpeople.observer.LoginViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import kotlinx.android.synthetic.main.activity_user_info.*
import java.io.File


class LastSignInFragment : Fragment() {

    private val vm by activityViewModels<LoginViewModel>()
    private lateinit var binding: LastSignInFragmentBinding
    private lateinit var imagePath: String
    private var file: File? = null
    private var gender = "남"
    private val fUserRepository: FUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }
    private val s3RemoteDataSource: S3RemoteDataSource by lazy {
        S3RemoteDataSourceImpl(
            binding.root.context,
            vm.customId.value.toString()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //updateUI(currentUser)
        return inflater.inflate(R.layout.last_sign_in_fragment, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.vm = vm
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerCallback()
        checkWrittenNeeds()

        binding.rgGender.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_man -> {
                    gender = "남"
                    Toast.makeText(context, "남자", Toast.LENGTH_SHORT).show()
                }
                R.id.rb_woman -> {
                    gender = "여"
                    Toast.makeText(context, "여자", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observerCallback() {

        vm.nextBtnEvent.observe(this, Observer {
            saveUser()
        })

        vm.galleryBtnEvent.observe(this, Observer {
            selectProfileImage()
        })

        vm.backBtnEvent.observe(this, Observer {
            val fragmentManager: FragmentManager =
                (activity as EmailSignInActivity).supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()
            fragmentManager.popBackStack()
        })
    }


    private fun checkWrittenNeeds() {
        binding.etNickname.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                btn_complete.background =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.bg_round_disable)
            } else {
                btn_complete.background =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.bg_round)
            }
        }
    }


    //회원정보 저장(retrofit2)
    private fun saveUser() {
        if (binding.etNickname.text.isEmpty()) {
            tv_alert.visibility = View.VISIBLE
        } else {
            FirebaseAuth.getInstance().currentUser?.let {
                if(!it.isEmailVerified){
                    Toast.makeText(context, resources.getText(R.string.authentication_mail), Toast.LENGTH_SHORT).show()
                } else {
                    //닉네임
                    val name = et_nickname.text.toString()
                    //소개 글
                    val introduce = et_introduce.text.toString()
                    //프로필 사진
                    val profileImage = file?.name
                    profileImage?.let {
                        s3RemoteDataSource.uploadWithTransferUtility(it, file, "profile")
                    }

                    fUserRepository.addUser(
                        FUser(
                            vm.customId.value.toString(),
                            vm.password.value.toString(),
                            name,
                            introduce,
                            gender,
                            profileImage,
                            0,
                            0,
                            false
                        ), {
                            val intent = Intent(activity, EmailLoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }, { e -> Log.e("sangmin_error", e) }
                    )
                }
            }
        }
    }

    //이미지Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    private fun getRealPathFromUri(uri: Uri): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor =
            (activity as EmailSignInActivity).contentResolver.query(uri, proj, null, null, null)!!
        val index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()

        return c.getString(index)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "외부 메모리 읽기/쓰기 사용 가능", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "외부 메모리 읽기/쓰기 제한", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_PROFILEIMG) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                try {
                    val uri: Uri = data!!.data!!
                    profile_image.setImageURI(uri)
                    imagePath = getRealPathFromUri(uri)
                    file = File(imagePath)
                    Log.d("sangmin-url", imagePath)
                } catch (e: Exception) {

                }
            } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                Toast.makeText(context, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }

    }

    private fun selectProfileImage() {
        //외부 쓰기 퍼미션이 있다면
        if (ContextCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                //갤러리 앱 실행
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                startActivityForResult(intent, CHOOSE_PROFILEIMG)

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        } else {
            ActivityCompat.requestPermissions(
                activity as EmailSignInActivity,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                100
            )
        }
    }

    companion object {
        private const val CHOOSE_PROFILEIMG = 200
    }
}
