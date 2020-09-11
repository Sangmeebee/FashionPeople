package com.sangmee.fashionpeople.ui.fragment

import android.content.res.AssetManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.ui.CategoryAdapter
import kotlinx.android.synthetic.main.fragment_tag.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class TagFragment : Fragment() {

    //assets 폴더에서 브랜드 파일 읽기용 프로퍼티
    private var inputStream: InputStream? = null
    private var manBrand = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        readFromAssets("man_brand.txt")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_select_image.setImageURI(resultUri)
        rv_category.adapter = CategoryAdapter()
    }

    //assets 폴더에서 브랜드 파일 읽기
    private fun readFromAssets(filename: String) {
        try {
            val am = resources.assets
            inputStream = am.open(filename, AssetManager.ACCESS_BUFFER)
            val reader = BufferedReader(InputStreamReader(inputStream))
            while (reader.readLine() != null) {
                manBrand.add(reader.readLine())
            }
        } catch (e: IOException) {
            Log.e("ERRORTAG", e.message.toString())
        } finally {
            inputStream?.close()
        }
    }

    fun setImageUri(resultUri : Uri){
        TagFragment.resultUri = resultUri
    }

    companion object {
        var resultUri :Uri? = null

    }
}
