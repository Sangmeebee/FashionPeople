package com.sangmee.fashionpeople.data

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.kakao.auth.KakaoSDK
import com.sangmee.fashionpeople.data.service.kakaologin.KakaoSDKAdapter

class GlobalApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
        instance = this
        KakaoSDK.init(KakaoSDKAdapter())

    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }

    fun getGlobalApplicationContext(): GlobalApplication {
        checkNotNull(instance) { "this application does not inherit com.kakao.GlobalApplication" }
        return instance!!
    }

    companion object {
        var instance: GlobalApplication? = null
        lateinit var prefs: PreferenceUtil
    }

}
