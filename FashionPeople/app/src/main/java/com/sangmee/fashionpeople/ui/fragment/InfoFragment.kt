package com.sangmee.fashionpeople.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import com.sangmee.fashionpeople.ui.TagActivity
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class InfoFragment : Fragment() {

    lateinit var customId: String
    var file: File? = null
    private val feedImageAdapter by lazy {
        FeedImageAdapter(customId)
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

        val battleImage = GlobalApplication.prefs.getString("battleImage", "")
        if(battleImage!=""){
            Glide.with(context!!)
                .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/${customId}/feed/${battleImage}")
                .error(R.drawable.plus).into(iv_plus)
            iv_plus.scaleType = ImageView.ScaleType.CENTER_CROP
        }



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
        btn_plus.setOnClickListener {
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
                GlobalApplication.prefs.setString("resultUri", resultUri.toString())
                val intent = Intent(activity, TagActivity::class.java)
                startActivity(intent)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e("TAG_ERROR", result.error.toString())
            }
        }
    }

    companion object {
        private const val CHOOSE_PROFILEIMG = 200
    }
}
