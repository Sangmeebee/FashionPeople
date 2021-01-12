package com.sangmee.fashionpeople.ui.add

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.S3RemoteDataSource
import com.sangmee.fashionpeople.data.dataSource.remote.S3RemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FeedImage
import kotlinx.android.synthetic.main.activity_tag.*
import org.jetbrains.anko.textColor
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TagActivity : AppCompatActivity() {

    private val loginType = GlobalApplication.prefs.getString("login_type", "empty")
    private val customId by lazy {
        GlobalApplication.prefs.getString(
            "${loginType}_custom_id",
            "empty"
        )
    }
    private val s3RemoteDataSource: S3RemoteDataSource by lazy {
        S3RemoteDataSourceImpl(
            applicationContext,
            customId
        )
    }

    private val vm by viewModels<TagViewModel>()

    override fun onResume() {
        super.onResume()
        initViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)

        lateinit var battleImageFile: File
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
            //서버에 사진 저장
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "JPEG_${customId}_${timeStamp}.jpg"
            val style = GlobalApplication.prefs.getString("style", "")
            val top = GlobalApplication.prefs.getString("top", "")
            val pants = GlobalApplication.prefs.getString("pants", "")
            val shoes = GlobalApplication.prefs.getString("shoes", "")
            val feedImage =
                FeedImage(
                    imageFileName,
                    null,
                    style,
                    top,
                    pants,
                    shoes,
                    null,
                    true,
                    null,
                    0.0F,
                    null,
                    null
                )

            vm.postFeedImage(customId, feedImage)

            //서버에 브랜드, 스타일 저장
            if (style != "") {
                vm.putStyle(style)
            }

            val brandSet = mutableSetOf<String>()
            if (top != "") {
                brandSet.add(top)
            }
            if (pants != "") {
                brandSet.add(pants)
            }
            if (shoes != "") {
                brandSet.add(shoes)
            }

            if (brandSet.isNotEmpty()) {
                for (brand in brandSet) {
                    vm.putBrand(brand)
                }
            }


            //S3에 저장
            s3RemoteDataSource.uploadWithTransferUtility(imageFileName, battleImageFile, "feed")
        }

    }

    private fun initViewModel() {
        vm.isComplete.observe(this, androidx.lifecycle.Observer {
            Toast.makeText(applicationContext, "사진 등록 완료", Toast.LENGTH_SHORT).show()
            finishActivity()
        })

        vm.isError.observe(this, androidx.lifecycle.Observer {
            Toast.makeText(applicationContext, "사진 등록 실패", Toast.LENGTH_SHORT).show()
            finishActivity()
        })
    }


    private fun finishActivity() {
        GlobalApplication.prefs.remove("resultUri")
        GlobalApplication.prefs.remove("style")
        GlobalApplication.prefs.remove("top")
        GlobalApplication.prefs.remove("pants")
        GlobalApplication.prefs.remove("shoes")
        finish()
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

    override fun onPause() {
        vm.unbindViewModel()
        super.onPause()
    }

    companion object {
        private const val CHOOSING_CATEGORY = 100
    }
}
