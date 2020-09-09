package com.sangmee.fashionpeople.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.ui.fragment.AlarmFragment
import com.sangmee.fashionpeople.ui.fragment.HomeFragment
import com.sangmee.fashionpeople.ui.fragment.InfoFragment
import com.sangmee.fashionpeople.ui.fragment.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, HomeFragment()).commit()
                }
                R.id.searchItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, SearchFragment()).commit()
                }
                R.id.addItem -> {

                }

                R.id.alarmItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, AlarmFragment()).commit()
                }
                R.id.infoItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, InfoFragment()).commit()
                }
            }
            return@setOnNavigationItemSelectedListener true

        }
    }
}
