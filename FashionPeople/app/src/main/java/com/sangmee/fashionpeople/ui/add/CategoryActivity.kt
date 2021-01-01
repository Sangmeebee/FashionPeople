package com.sangmee.fashionpeople.ui.add

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

class CategoryActivity : AppCompatActivity(), OnListItemSelectedInterface {

    //assets 폴더에서 브랜드 파일 읽기용 프로퍼티
    private var categoryList = arrayListOf<String>()
    private val categoryAdapter = CategoryAdapter(this)
    private var correctBrandList = arrayListOf<String>()

    private val subject by lazy {
        intent.getStringExtra("subject")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        if (subject == "style") {
            readFromAssets("style.txt")
        } else {
            readFromAssets("man_brand.txt")
        }
        categoryAdapter.setTitleList(categoryList)

        //edittext 리스너
        et_brand.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val correctBrand = et_brand.text.toString().toLowerCase(Locale.getDefault())
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(
                        CoroutineScope(Dispatchers.Default).coroutineContext
                    ) {
                        // background thread
                        correctBrandList = search(correctBrand)
                    }
                    categoryAdapter.setTitleList(correctBrandList)
                }

            }
        })


        //리사이클러뷰 세팅
        rv_brand.apply {
            setHasFixedSize(true)
            adapter = categoryAdapter
        }

        iv_cancel.setOnClickListener {
            finish()
        }
    }

    //리사이클러뷰에서 맞는 text 찾기
    fun search(query: String): ArrayList<String> {

        val tempList = arrayListOf<String>()
        for (brand in categoryList) {
            if (brand.toLowerCase().contains(query)) {
                tempList.add(brand)
            }
        }
        return tempList
    }

    //assets 폴더에서 브랜드 파일 읽기
    private fun readFromAssets(filename: String) {
        var inputStream: InputStream? = null
        try {
            val am = resources.assets
            inputStream = am.open(filename)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            do {
                line = reader.readLine()
                if (line != null) {
                    categoryList.add(line)
                }
            } while (line != null)
        } catch (e: IOException) {
            Log.e("ERRORTAG", e.message.toString())
        } finally {
            inputStream?.close()
        }
    }

    override fun onItemSelected(title: String) {
        GlobalApplication.prefs.setString(subject, title)
        val intent = intent
        intent.putExtra("subject", subject)
        setResult(RESULT_OK, intent)
        finish()
    }
}
