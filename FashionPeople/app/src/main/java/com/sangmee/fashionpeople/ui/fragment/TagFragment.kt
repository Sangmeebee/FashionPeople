package com.sangmee.fashionpeople.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sangmee.fashionpeople.R
import kotlinx.android.synthetic.main.fragment_tag.*

class TagFragment : Fragment() {


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

        tv_category_name.setOnClickListener {

        }
    }

    fun setImageUri(resultUri: Uri) {
        TagFragment.resultUri = resultUri
    }

    companion object {
        var resultUri: Uri? = null

    }
}
