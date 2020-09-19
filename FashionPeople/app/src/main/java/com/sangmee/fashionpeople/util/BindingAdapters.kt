package com.sangmee.fashionpeople.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import com.sangmee.fashionpeople.R

@BindingAdapter("bindToolbar")
fun bindToolbar(toolbar: Toolbar, activity: AppCompatActivity) {
    with(activity) {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow)
            title = getString(R.string.setting_text)
        }
    }
}