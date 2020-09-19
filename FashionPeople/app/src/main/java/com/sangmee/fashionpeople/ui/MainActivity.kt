package com.sangmee.fashionpeople.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.ui.fragment.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeItem -> {
                    if (currentFragment != "homeItem") {
                        replaceFragment(HomeFragment())
                        currentFragment = "homeItem"
                    }
                }
                R.id.searchItem -> {
                    if (currentFragment != "searchItem") {
                        replaceFragment(SearchFragment())
                        currentFragment = "searchItem"
                    }
                }
                R.id.addItem -> {
                    replaceFragment(TagFragment())
                }

                R.id.alarmItem -> {
                    if (currentFragment != "alarmItem") {
                        replaceFragment(AlarmFragment())
                        currentFragment = "alarmItem"
                    }
                }
                R.id.infoItem -> {
                    if (currentFragment != "infoItem") {
                        replaceFragment(InfoFragment())
                        currentFragment = "infoItem"
                    }
                }

            }
            return@setOnNavigationItemSelectedListener true

        }
    }

    //fragment 교체
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment).commit()
    }


    companion object {
        private var currentFragment = "homeItem"
    }
}
