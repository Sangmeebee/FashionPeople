package com.sangmee.fashionpeople.ui.fragment

import android.Manifest
import android.content.Context
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
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import com.sangmee.fashionpeople.retrofit.RetrofitClient
import com.sangmee.fashionpeople.retrofit.model.FUser
import com.sangmee.fashionpeople.retrofit.model.FeedImage
import com.sangmee.fashionpeople.ui.FeedImageAdapter
import com.sangmee.fashionpeople.ui.LoginActivity
import com.sangmee.fashionpeople.ui.MainActivity
import com.sangmee.fashionpeople.ui.SettingActivity
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.support.v4.startActivityForResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class InfoFragment : Fragment()  {

    lateinit var customId: String
    var file: File? = null
    private val feedImageAdapter by lazy {
        FeedImageAdapter(customId)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customId = GlobalApplication.prefs.getString("custom_id", "")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runBlocking {
            val a = launch {
                RetrofitClient().getFUserService().getFUser(customId)
                    .enqueue(object : Callback<FUser> {
                        override fun onFailure(call: Call<FUser>, t: Throwable) {
                            Log.d("sangmin_error", t.message)
                        }

                        override fun onResponse(call: Call<FUser>, response: Response<FUser>) {
                            //닉네임 레트로핏으로 불러오기
                            val profileImgName = response.body()?.profileImage.toString()
                            val introduce = response.body()?.instagramId
                            val userId = response.body()?.name
                            val profileImg = view.findViewById<ImageView>(R.id.iv_info_user)
                            val introduceTv =
                                view.findViewById<TextView>(R.id.tv_info_user_introduce)
                            val userName = view.findViewById<TextView>(R.id.tv_info_user_name)

                            introduce?.let {
                                introduceTv.text = it
                            }

                            userId?.let {
                                userName.text = it
                            }

                            Glide.with(context!!)
                                .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${customId}/profile/${profileImgName}")
                                .apply(RequestOptions().circleCrop())
                                .error(R.drawable.user).into(profileImg)

                        }
                    })
            }
            a.join()
        }
        //사진 등록 imageView 클릭시 이벤트
        btn_info_like.setOnClickListener {
            if (context?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                } == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context!!,
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
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it,
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        100
                    )
                }
            }
        }

        rv_user_image.apply {
            adapter = feedImageAdapter
        }

        btn_setting.setOnClickListener{
            val intent = Intent(context, SettingActivity::class.java)
            startActivityForResult(intent, LOGOUT_CODE)
        }

        getFeedImages()


    }

    private fun getFeedImages() {
        if (customId != "") {
            RetrofitClient().getFeedImageService().getFeedImages(customId)
                .enqueue(object : Callback<List<FeedImage>> {
                    override fun onResponse(
                        call: Call<List<FeedImage>>,
                        response: Response<List<FeedImage>>
                    ) {
                        response.body()?.let { feedImages ->
                            feedImageAdapter.setFeedImages(feedImages)
                        }
                    }

                    override fun onFailure(call: Call<List<FeedImage>>, t: Throwable) {
                        Log.d("fail", t.message)
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_PROFILEIMG && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            try {
                val uri: Uri = data.data!!
                CropImage.activity(uri)
                    .setAspectRatio(1, 1)
                    .start(context!!, this)
            } catch (e: Exception) {

            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val resultUri = result.uri
                //TagFragment로 이동 & resultUri 전
                val mActivity = activity as MainActivity
                val fragment = TagFragment()
                fragment.setImageUri(resultUri)
                mActivity.replaceFragment(TagFragment())

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e("TAG_ERROR", result.error.toString())
            }
        }
        if(requestCode == LOGOUT_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            activity?.finish()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    //이미지Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    private fun getRealPathFromUri(uri: Uri): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor = context!!.contentResolver.query(uri, proj, null, null, null)!!
        val index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()
        return c.getString(index)
    }

    companion object {
        private const val CHOOSE_PROFILEIMG = 200
        private const val LOGOUT_CODE = 210
    }




}
