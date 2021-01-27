package com.sangmee.fashionpeople.ui.add

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.data.dataSource.remote.S3RemoteDataSource
import com.sangmee.fashionpeople.data.dataSource.remote.S3RemoteDataSourceImpl
import com.sangmee.fashionpeople.data.model.FeedImage
import kotlinx.android.synthetic.main.activity_tag.*
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
            val content = tv_style_name.text.toString()
            redirectCategoryActivity("style", content)
        }

        tv_top_brand_name.setOnClickListener {
            val content = tv_top_brand_name.text.toString()
            redirectCategoryActivity("top", content)
        }

        tv_pants_brand_name.setOnClickListener {
            val content = tv_pants_brand_name.text.toString()
            redirectCategoryActivity("pants", content)
        }

        tv_shoes_brand_name.setOnClickListener {
            val content = tv_shoes_brand_name.text.toString()
            redirectCategoryActivity("shoes", content)
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
                FeedImage(imageFileName, style, top, pants, shoes, true, 0.0F)

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

    private fun redirectCategoryActivity(subject: String, content: String) {
        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra("subject", subject)
        intent.putExtra("content", content)
        startActivityForResult(intent, CHOOSING_CATEGORY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSING_CATEGORY && resultCode == RESULT_OK) {
            when (data?.getStringExtra("subject")) {
                "style" -> {
                    tv_style_name.apply {
                        val content = GlobalApplication.prefs.getString("style", "")
                        if (content == "") {
                            text = "선택안함"
                            setTextColor(ContextCompat.getColor(context, R.color.brandTextColor))
                        } else {
                            text = content
                            setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                        }
                    }
                }
                "top" -> {
                    tv_top_brand_name.apply {
                        val content = GlobalApplication.prefs.getString("top", "")
                        if (content == "") {
                            text = "선택안함"
                            setTextColor(ContextCompat.getColor(context, R.color.brandTextColor))
                        } else {
                            text = content
                            setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                        }
                    }
                }
                "pants" -> {
                    tv_pants_brand_name.apply {
                        val content = GlobalApplication.prefs.getString("pants", "")
                        if (content == "") {
                            text = "선택안함"
                            setTextColor(ContextCompat.getColor(context, R.color.brandTextColor))
                        } else {
                            text = content
                            setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                        }
                    }
                }
                "shoes" -> {
                    tv_shoes_brand_name.apply {
                        val content = GlobalApplication.prefs.getString("shoes", "")
                        if (content == "") {
                            text = "선택안함"
                            setTextColor(ContextCompat.getColor(context, R.color.brandTextColor))
                        } else {
                            text = content
                            setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                        }
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
