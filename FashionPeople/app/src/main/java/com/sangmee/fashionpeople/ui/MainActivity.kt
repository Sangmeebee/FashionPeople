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
        
        navigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeItem -> {
                    if (currentFragment != "homeItem") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, HomeFragment()).commit()
                        currentFragment = "homeItem"
                    }
                }
                R.id.searchItem -> {
                    if (currentFragment != "searchItem") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, SearchFragment()).commit()
                        currentFragment = "searchItem"
                    }

                }
                R.id.addItem -> {
                }

                R.id.alarmItem -> {
                    if (currentFragment != "alarmItem") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, AlarmFragment()).commit()
                        currentFragment = "alarmItem"
                    }
                }
                R.id.infoItem -> {
                    if (currentFragment != "infoItem") {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, InfoFragment()).commit()
                        currentFragment = "infoItem"
                    }
                }

            }
            return@setOnNavigationItemSelectedListener true

        }
    }

    companion object {
        private var currentFragment = "homeItem"
    }
}
