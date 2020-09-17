package com.sangmee.fashionpeople.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import kotlinx.android.synthetic.main.activity_tag.*

class TagActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)

        val resultUri: Uri by lazy {
            val resultUriStr = intent.getStringExtra("resultUri")
            Uri.parse(resultUriStr)
        }

        iv_select_image.setImageURI(resultUri)

        tv_category_name.text = GlobalApplication.prefs.getString("category", "")

        tv_category_name.setOnClickListener {
            redirectCategoryActivity("category")
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
            GlobalApplication.prefs.remove("category")
            GlobalApplication.prefs.remove("top")
            GlobalApplication.prefs.remove("pants")
            GlobalApplication.prefs.remove("shoes")
        }

    }

    private fun redirectCategoryActivity(subject: String){
        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra("subject", subject)
        startActivity(intent)
    }
}
