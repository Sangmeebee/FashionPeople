package com.sangmee.fashionpeople.ui

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import kotlinx.android.synthetic.main.activity_tag.*
import org.jetbrains.anko.textColor

class TagActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)

        lateinit var resultUri: Uri
        val resultUriStr = GlobalApplication.prefs.getString("resultUri", "")
        if (resultUriStr != "") {
            resultUri = Uri.parse(resultUriStr)
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
            GlobalApplication.prefs.remove("resultUri")
            GlobalApplication.prefs.remove("style")
            GlobalApplication.prefs.remove("top")
            GlobalApplication.prefs.remove("pants")
            GlobalApplication.prefs.remove("shoes")
            finish()
        }

    }

    private fun redirectCategoryActivity(subject: String) {
        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra("subject", subject)
        startActivityForResult(intent, CHOOSING_CATEGORY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSING_CATEGORY && resultCode == RESULT_OK) {
            when(data?.getStringExtra("subject")){
                "style" -> {
                    tv_style_name.apply{
                        text = GlobalApplication.prefs.getString("style", "")
                        textColor = R.color.colorBlack
                    }
                }
                "top" -> {
                    tv_top_brand_name.apply{
                        text = GlobalApplication.prefs.getString("top", "")
                        textColor = R.color.colorBlack
                    }
                }
                "pants" ->{
                    tv_pants_brand_name.apply{
                        text = GlobalApplication.prefs.getString("pants", "")
                        textColor = R.color.colorBlack
                    }
                }
                "shoes" ->{
                    tv_shoes_brand_name.apply {
                        text = GlobalApplication.prefs.getString("shoes", "")
                        textColor = R.color.colorBlack
                    }
                }
            }
        }
    }

    //이미지Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    private fun getRealPathFromUri(uri: Uri): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor = contentResolver.query(uri, proj, null, null, null)!!
        val index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()
        return c.getString(index)
    }

    companion object {
        private const val CHOOSING_CATEGORY = 100
    }
}
