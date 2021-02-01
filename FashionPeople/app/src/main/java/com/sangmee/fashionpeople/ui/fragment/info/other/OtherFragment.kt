package com.sangmee.fashionpeople.ui.fragment.info.other

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
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.dataSource.remote.S3RemoteDataSource
import com.sangmee.fashionpeople.data.dataSource.remote.S3RemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FUser
import com.sangmee.fashionpeople.databinding.FragmentOtherBinding
import com.sangmee.fashionpeople.observer.InfoViewModel
import com.sangmee.fashionpeople.observer.MainViewModel
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.fragment.info.follow.FollowFragment
import com.sangmee.fashionpeople.ui.fragment.info.image_content.ViewPagerAdapter
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_info.*
import java.io.File


private const val CUSTOM_ID = "param1"

class OtherFragment : Fragment() {
    private var customId: String? = null

    //otherfragment에서 내 계정 인지 판단
    var isMe = false

    lateinit var binding: FragmentOtherBinding
    private val infoVm: InfoViewModel by viewModels()
    private val mainVm by activityViewModels<MainViewModel>()
    private lateinit var file: File
    private val s3RemoteDataSource: S3RemoteDataSource by lazy {
        S3RemoteDataSourceImpl(
            binding.root.context,
            customId!!
        )
    }
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            customId = it.getString(CUSTOM_ID)
            if (customId == infoVm.customId) {
                isMe = true
            }
            Log.d("FSP_CUSTOM_ID", customId.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        customId?.let {
            infoVm.callProfile(it)
            infoVm.callIsFollowing(it)
        }
        return inflater.inflate(R.layout.fragment_other, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
            binding.isMe = isMe
            binding.apply {
                customId = this@OtherFragment.customId
                otherVm = this@OtherFragment.infoVm
                lifecycleOwner = viewLifecycleOwner
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTabLayout()
        observeCallBack()
    }

    private fun setTabLayout() {
        viewPager.adapter = ViewPagerAdapter(this, customId!!)

        TabLayoutMediator(tl_container, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setIcon(R.drawable.photo_library_selector)
                }
                else -> {
                    tab.setIcon(R.drawable.scrap_selector)
                }
            }
        }.attach()
    }

    private fun observeCallBack() {
        infoVm.followBtnEvent.observe(viewLifecycleOwner, Observer {
            btnForFollowing()
        })
        infoVm.callActivity.observe(viewLifecycleOwner, Observer {
            (activity as MainActivity).replaceFragmentUseTagBackStack(
                FollowFragment.newInstance(
                    it,
                    customId!!,
                    infoVm.userName.value!!,
                    infoVm.followerNum.value!!,
                    infoVm.followingNum.value!!
                ), mainVm.tagName.value!!
            )
        })

        infoVm.galleryBtnEvent.observe(this, Observer {
            if (isMe) {
                selectProfileImage()
            }
        })

        infoVm.isCallProfileComplete.observe(viewLifecycleOwner, Observer {
            crossfade()
        })

        infoVm.introduce.observe(viewLifecycleOwner, Observer {
            binding.tvIntroduce.isVisible = !it.isNullOrEmpty()
        })

        infoVm.behaviorSubject
            .observeOn(Schedulers.io())
            .subscribe { saveImageToServer(file) }
            .addTo(compositeDisposable)
    }

    private fun btnForFollowing() {
        customId?.let { customId ->
            infoVm.isFollowing.value?.let { isFollowing ->
                if (isFollowing) {
                    mainVm.deleteFollowing(customId)
                    mainVm.followingNum.value = mainVm.followingNum.value!! - 1
                    setMyBtn(customId, false)
                    infoVm.followerNum.value = infoVm.followerNum.value!! - 1
                    infoVm.isFollowing.value = !isFollowing
                } else {
                    mainVm.updateFollowing(customId)
                    mainVm.followingNum.value = mainVm.followingNum.value!! + 1
                    setMyBtn(customId, true)
                    infoVm.followerNum.value = infoVm.followerNum.value!! + 1
                    infoVm.isFollowing.value = !isFollowing
                }
            }
        }
    }

    private fun setMyBtn(customId: String, isFollow: Boolean) {
        mainVm.isFollowingsFollowing.value?.let {
            it[customId] = isFollow
            mainVm.isFollowingsFollowing.value = it
        }
        mainVm.isFollowingsFollower.value?.let {
            it[customId] = isFollow
            mainVm.isFollowingsFollower.value = it
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CHOOSE_PROFILEIMG) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                try {
                    val uri: Uri = data!!.data!!
                    val imagePath = getRealPathFromUri(uri)
                    file = File(imagePath)
                    infoVm.behaviorSubject.onNext(Unit)
                    binding.ivProfile.setImageURI(uri)
                } catch (e: Exception) {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                Toast.makeText(context, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    private fun crossfade() {
        binding.clContainer.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(800L)
                .setListener(null)
        }
    }

    private fun saveImageToServer(file: File?) {
        //프로필 사진 s3에 저장
        Log.d("Sangmeebee", "Hi")
        val profileImage = file?.name
        profileImage?.let {
            s3RemoteDataSource.uploadWithTransferUtility(it, file, "profile")
        }
        val fUser = FUser(
            customId,
            infoVm.userName.value,
            infoVm.introduce.value,
            infoVm.gender.value,
            infoVm.height.value,
            infoVm.weight.value,
            profileImage,
            null,
            null
        )
        infoVm.updateProfile(customId!!, fUser)

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

    companion object {
        private const val CHOOSE_PROFILEIMG = 200


        @JvmStatic
        fun newInstance(customId: String) =
            OtherFragment().apply {
                arguments = Bundle().apply {
                    putString(CUSTOM_ID, customId)
                }
            }
    }
}
