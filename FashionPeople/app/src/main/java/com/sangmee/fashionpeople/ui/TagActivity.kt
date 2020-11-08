package com.sangmee.fashionpeople.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import com.sangmee.fashionpeople.retrofit.RetrofitClient
import com.sangmee.fashionpeople.retrofit.model.FeedImage
import kotlinx.android.synthetic.main.activity_tag.*
import org.jetbrains.anko.textColor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class TagActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)

        lateinit var battleImageFile: File
        val customId = GlobalApplication.prefs.getString("custom_id", "")
        lateinit var resultUri: Uri
        val resultUriStr = GlobalApplication.prefs.getString("resultUri", "")
        if (resultUriStr != "") {
            resultUri = Uri.parse(resultUriStr)
            battleImageFile = File(resultUri.path)
        }

        iv_select_image.setImageURI(resultUri)

        tv_style_name.setOnClickListener {
            redirectCategoryActivity("style")
        }

        tv_top_brand_name.setOnClickListener {
            redirectCategoryActivity("top")
        }

        tv_pants_brand_name.setOnClickListener {
            redirectCategoryActivity("pants")
        }

        tv_shoes_brand_name.setOnClickListener {
            redirectCategoryActivity("shoes")
        }




        btn_complete.setOnClickListener {
            //서버에 저장
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "JPEG_$timeStamp.jpg"
            val style = GlobalApplication.prefs.getString("style", "")
            val top = GlobalApplication.prefs.getString("top", "")
            val pants = GlobalApplication.prefs.getString("pants", "")
            val shoes = GlobalApplication.prefs.getString("shoes", "")
            val feedImage =
                FeedImage(imageFileName, timeStamp, style, top, pants, shoes, null, true, null, null)

            RetrofitClient().getFeedImageService().postFeedImage(customId, feedImage)
                .enqueue(object : Callback<FeedImage> {
                    override fun onResponse(call: Call<FeedImage>, response: Response<FeedImage>) {
                        showMessage()
                    }

                    override fun onFailure(call: Call<FeedImage>, t: Throwable) {
                    }
                })

            //S3에 저장
            uploadWithTransferUtility(customId, imageFileName, battleImageFile)

            GlobalApplication.prefs.setString("battleImage", imageFileName)
            GlobalApplication.prefs.remove("resultUri")
            GlobalApplication.prefs.remove("style")
            GlobalApplication.prefs.remove("top")
            GlobalApplication.prefs.remove("pants")
            GlobalApplication.prefs.remove("shoes")
            finish()
        }

    }

    private fun showMessage() {
        Toast.makeText(this, "사진 등록 완료", Toast.LENGTH_SHORT).show()
    }

    private fun redirectCategoryActivity(subject: String) {
        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra("subject", subject)
        startActivityForResult(intent, CHOOSING_CATEGORY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSING_CATEGORY && resultCode == RESULT_OK) {
            when (data?.getStringExtra("subject")) {
                "style" -> {
                    tv_style_name.apply {
                        text = GlobalApplication.prefs.getString("style", "")
                        textColor = R.color.colorBlack
                    }
                }
                "top" -> {
                    tv_top_brand_name.apply {
                        text = GlobalApplication.prefs.getString("top", "")
                        textColor = R.color.colorBlack
                    }
                }
                "pants" -> {
                    tv_pants_brand_name.apply {
                        text = GlobalApplication.prefs.getString("pants", "")
                        textColor = R.color.colorBlack
                    }
                }
                "shoes" -> {
                    tv_shoes_brand_name.apply {
                        text = GlobalApplication.prefs.getString("shoes", "")
                        textColor = R.color.colorBlack
                    }
                }
            }
        }
    }

    //s3저장 메소드
    //aws s3에 이미지 업로드
    private fun uploadWithTransferUtility(customId: String, fileName: String, file: File?) {

        val credentialsProvider = CognitoCachingCredentialsProvider(
            applicationContext,
            "ap-northeast-2:04a21c16-627a-49a9-8229-f1c412ddebfa",  // 자격 증명 풀 ID
            Regions.AP_NORTHEAST_2 // 리전
        )

        TransferNetworkLossHandler.getInstance(applicationContext)

        val transferUtility = TransferUtility.builder()
            .context(applicationContext)
            .defaultBucket(BUCKET_NAME)
            .s3Client(AmazonS3Client(credentialsProvider, Region.getRegion(Regions.AP_NORTHEAST_2)))
            .build()

        /* Store the new created Image file path */

        val uploadObserver = transferUtility.upload(
            "users/${customId}/feed/${fileName}",
            file,
            CannedAccessControlList.PublicRead
        )

        //CannedAccessControlList.PublicRead 읽기 권한 추가

        // Attach a listener to the observer
        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    Log.d("MYTAG", "fileupload")
                }
            }

            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                val done = (((current.toDouble() / total) * 100.0).toInt())
                Log.d("MYTAG", "UPLOAD - - ID: $id, percent done = $done")
            }

            override fun onError(id: Int, ex: Exception) {
                Log.d("MYTAG", "UPLOAD ERROR - - ID: $id - - EX: ${ex.message.toString()}")
            }
        })

        // If you prefer to long-poll for updates
        if (uploadObserver.state == TransferState.COMPLETED) {
            /* Handle completion */
        }
    }


    companion object {
        private const val CHOOSING_CATEGORY = 100
        private const val BUCKET_NAME = "fashionprofile-images"
    }
}
