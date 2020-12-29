package com.sangmee.fashionpeople.ui.fragment.info

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.FUserRemoteDataSourceImpl
import com.sangmee.fashionpeople.data.dataSource.remote.S3RemoteDataSource
import com.sangmee.fashionpeople.data.dataSource.remote.S3RemoteDataSourceImpl
import com.sangmee.fashionpeople.data.repository.FUserRepository
import com.sangmee.fashionpeople.data.repository.FUserRepositoryImpl
import com.sangmee.fashionpeople.databinding.FragmentInfoBinding
import com.sangmee.fashionpeople.observer.InfoViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.info.follow.FollowFragment
import com.sangmee.fashionpeople.ui.fragment.info.image_content.ViewPagerAdapter
import com.sangmee.fashionpeople.ui.login.LoginActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_info.*
import java.io.File

class InfoFragment : Fragment() {

    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    val customId by lazy { GlobalApplication.prefs.getString("${loginType}_custom_id", "") }
    lateinit var binding: FragmentInfoBinding
    private val vm: InfoViewModel by viewModels()
    private val s3RemoteDataSource: S3RemoteDataSource by lazy {
        S3RemoteDataSourceImpl(
            binding.root.context,
            customId
        )
    }
    private val fUserRepository: FUserRepository by lazy {
        FUserRepositoryImpl(FUserRemoteDataSourceImpl())
    }
    private val compositeDisposable = CompositeDisposable()
    private lateinit var file: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.vm = vm
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelCallback()
        vm.callProfile(vm.customId)

        //자기소개글 있는지 판단
        vm.introduce.value?.let {
            if (it.isNotEmpty()) {
                vm.isInvisible.value = true
            }
        }
        binding.isInvisible = vm.isInvisible.value

        //툴바 세팅
        setToolbar(binding.tbProfile)
        setHasOptionsMenu(true)
        //tablayout 세팅
        viewPager.adapter = ViewPagerAdapter(this, customId)

        TabLayoutMediator(tl_container, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setIcon(R.drawable.photo_library_selector)
                }
                else -> {
                    tab.setIcon(R.drawable.photo_saved_selector)
                }
            }
        }.attach()
    }

    //메뉴 버튼 세팅
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.setting_toolbar, menu)
    }

    //메뉴 버튼 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> {
                Toast.makeText(context, "click", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGOUT_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            activity?.finish()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        if (requestCode == CHOOSE_PROFILEIMG) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                try {
                    val uri: Uri = data!!.data!!
                    val imagePath = getRealPathFromUri(uri)
                    file = File(imagePath)
                    vm.publishSubject.onNext(Unit)
                    binding.ivProfile.setImageURI(uri)
                } catch (e: Exception) {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                Toast.makeText(context, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    private fun viewModelCallback() {

        vm.galleryBtnEvent.observe(this, Observer {
            selectProfileImage()
        })

        vm.callActivity.observe(viewLifecycleOwner, Observer {
            (activity as MainActivity).replaceFragmentUseBackStack(
                FollowFragment.newInstance(
                    it,
                    customId,
                    vm.userName.value!!,
                    vm.followerNum.value!!,
                    vm.followingNum.value!!
                )
            )
        })

        vm.publishSubject
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .subscribe { saveImageToServer(file) }
            .addTo(compositeDisposable)
    }

    private fun saveImageToServer(file: File?) {
        //프로필 사진 s3에 저장
        Log.d("Sangmeebee", "Hi")
        val profileImage = file?.name
        profileImage?.let {
            s3RemoteDataSource.uploadWithTransferUtility(it, file, "profile")
        }

        fUserRepository.getFUser(customId, { user ->
            profileImage?.let { user.profileImage = it }
            fUserRepository.updateUser(customId, user, {}, {})
        }, { error -> Log.d("CALL_PROFILE_ERROR", error) })
    }

    //이미지Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    private fun getRealPathFromUri(uri: Uri): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor =
            (activity as MainActivity).contentResolver.query(uri, proj, null, null, null)!!
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
                activity as MainActivity,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                100
            )
        }
    }

    private fun setToolbar(toolbar: Toolbar) {
        (activity as MainActivity).setSupportActionBar(toolbar)
        (activity as MainActivity).supportActionBar?.run {
            setDisplayShowTitleEnabled(false)
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    companion object {
        private const val CHOOSE_PROFILEIMG = 200
        private const val LOGOUT_CODE = 210
    }


}
